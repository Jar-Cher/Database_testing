package org.example
import kotlinx.coroutines.*
import kotlin.math.*

const val selection = "SELECT * FROM registered_persons WHERE name='B' limit 10"
const val insertion = "INSERT INTO \"registered_persons\" (surname, name) VALUES ('A', 'B')"
const val update = "UPDATE registered_persons SET surname='C' WHERE name='B'"

fun main(args: Array<String>) {
    test(10000,"READ COMMITTED")
    test(10000,"REPEATABLE READ")
    test(10000,"SERIALIZABLE")
}