package org.example

fun main(args: Array<String>) {
    val tester = DBConnection("jdbc:postgresql://localhost:5432/culture_fest", login, password)

    tester.disconnect()
}