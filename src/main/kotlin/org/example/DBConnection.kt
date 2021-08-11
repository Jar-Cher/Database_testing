package org.example

import java.lang.IllegalStateException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class DBConnection(url: String, login: String, password: String, isolationLevel: String) {
    private lateinit var connection: Connection
    private lateinit var statement: Statement
    init {
        try {
            connection = DriverManager.getConnection(url, login, password)
            statement = connection.createStatement()
            statement.execute("SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL $isolationLevel")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun disconnect() {
        statement.close()
        connection.close()
    }
    fun executeTransaction(times: Int, transaction: String): Pair<Int,MutableList<Double>> {
        val ans = mutableListOf<Double>()
        var i = 1
        var fails = 0
        while (i<times) {
            val startTime = System.nanoTime()
            try {
                statement.executeUpdate(transaction)
                i++
            } catch (e: SQLException) {
                when {
                    e.sqlState=="40001" -> fails ++
                    e.sqlState!="0100E" -> throw IllegalStateException(e.message)
                    else -> i++
                }
            }

            ans.add((System.nanoTime() - startTime) / 1000.0)
        }
        return Pair(fails, ans)
    }
}