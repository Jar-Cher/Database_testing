package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.sqrt
import kotlin.math.pow

fun printInfo(name: String, info: Pair<Int,MutableList<Double>>, isolationLevel: String) {
    println("$name:")
    println("Isolation Level: $isolationLevel")
    println("Total time spent, ms: " + info.second.sum())
    println("Total collisions: " + info.first)
    println("Shortest transaction: " + info.second.minOrNull())
    val avg = info.second.average()
    println("Average time spent, ms: $avg")
    println("Longest transaction: " + info.second.maxOrNull())
    val diff = info.second.fold(0.0) { prevResult, element -> prevResult + (avg - element).pow(2) }
    val sd = sqrt(diff / (info.second.size - 1))
    println("Standard deviation: $sd")
    println()
}

fun test(times: Int, isolationLevel: String) {
    val tester1 = DBConnection("jdbc:postgresql://localhost:5432/culture_fest", login, password, isolationLevel)
    val tester2 = DBConnection("jdbc:postgresql://localhost:5432/culture_fest", login, password, isolationLevel)
    val tester3 = DBConnection("jdbc:postgresql://localhost:5432/culture_fest", login, password, isolationLevel)
    var selectionTimes = Pair(0, mutableListOf<Double>())
    var insertionTimes = Pair(0, mutableListOf<Double>())
    var updateTimes = Pair(0, mutableListOf<Double>())
    runBlocking(Dispatchers.Default) {
        launch {
            selectionTimes = tester1.executeTransaction(times, insertion)
        }
        launch {
            insertionTimes = tester2.executeTransaction(times, selection)
        }
        launch {
            updateTimes = tester3.executeTransaction(times, update)
        }
    }
    tester1.disconnect()
    tester2.disconnect()
    tester3.disconnect()
    printInfo("Selection", selectionTimes, isolationLevel)
    printInfo("Insertion", insertionTimes, isolationLevel)
    printInfo("Update", updateTimes, isolationLevel)
    val garbageDeleter = DBConnection("jdbc:postgresql://localhost:5432/culture_fest", login, password, isolationLevel)
    garbageDeleter.executeTransaction(1, "DELETE FROM registered_persons WHERE name='B' AND surname='A'")
}
