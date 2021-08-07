package org.example

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class DBConnection(url: String, login: String, password: String) {
    private lateinit var connection: Connection
    private lateinit var statement: Statement
    init {
        try {
            connection = DriverManager.getConnection(url, login, password)
            statement = connection.createStatement()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun disconnect() {
        statement.close()
        connection.close()
    }
}