@file:Suppress("KotlinConstantConditions")

import java.io.File
import java.io.InputStream
import java.lang.IndexOutOfBoundsException

const val movements = 5002

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val input = inputStream.bufferedReader()
        .use {
            it.readText().split("\r\n")
                .map { e -> e.split("").subList(1, e.length + 1) }
        }
    var start = Pair(0, 0)
    for (rowIndex in input.indices) {
        if (input[rowIndex].contains("S")) {
            start = Pair(rowIndex, input[rowIndex].indexOf("S"))
        }
    }
    val memory = mutableMapOf<Set<Pair<Int, Int>>, Map<Pair<Int, Int>, Set<Pair<Int, Int>>>>()

    var moves = mutableMapOf(Pair(0, 0) to mutableSetOf(start))
    val that = Pair(0, 0)

    for (i in 0..<movements) {
        val temp = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()
        for (map in moves.keys) {
            val outputMove = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()
//            if (memory.keys.contains(moves[map]!!)) {
//                val memoryPart = moves[map]!!.toSet()
//                for (part in memory[memoryPart]!!) {
//                    val newMove = Pair(map.first + part.key.first, map.second + part.key.second)
//                    if (!outputMove.keys.contains(newMove))
//                        outputMove[newMove] = mutableSetOf()
//                    outputMove[newMove]!!.addAll(part.value.toMutableSet())
//                }
//            } else {
                for (actual in moves[map]!!) {
                    for (move in listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
                        var newMove = Pair(actual.first + move.first, actual.second + move.second)
                        try {
                            val isOn = input[newMove.first][newMove.second]
                            if (isOn != "#") {
                                if (!outputMove.keys.contains(that))
                                    outputMove[that] = mutableSetOf()
                                outputMove[that]!!.add(newMove)
                            }
                        } catch (_: IndexOutOfBoundsException) {
                            if (move.first > 0) {
                                newMove = Pair(0, newMove.second)
                            } else if (move.first < 0) {
                                newMove = Pair(input.lastIndex, newMove.second)
                            } else if (move.second > 0) {
                                newMove = Pair(newMove.first, 0)
                            } else if (move.second < 0) {
                                newMove = Pair(newMove.first, input.lastIndex)
                            }
                            if (outputMove.keys.contains(move))
                                outputMove[move]!!.add(newMove)
                            else
                                outputMove[move] = mutableSetOf(newMove)
                        }
                    }
                }
//                memory[moves[map]!!.toSet()] = outputMove
//            }
            for (j in outputMove.keys) {
                val position = Pair(map.first + j.first, map.second + j.second)
                if (position !in temp.keys)
                    temp[position] = mutableSetOf()
                temp[position]!!.addAll(outputMove[j]!!)
            }
        }
        moves = temp
//        if (i+1 in listOf(6, 10, 50, 100, 500, 1000, 5000)){
        if ((i + 1) % 1 == 0) {
            print("${i + 1}: ")
            println(moves.values.sumOf { e -> e.count().toULong() })
//            show(input, moves)
        }
    }
    print("$movements: ")
    if (movements % 2 == 0)
        println(moves.values.sumOf { e -> e.count().toULong() })
    else
        println(moves.values.sumOf { e -> e.count().toULong() })
}

fun show(input: List<List<String>>, oddMove: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>) {
    for (moves in oddMove.keys) {
        println(moves)
        for (row in input.indices) {
            for (column in input[row].indices) {
                if (oddMove[moves]!!.contains(Pair(row, column)))
                    print("0")
                else
                    print(input[row][column])
            }
            println()
        }
        println()
    }
}
