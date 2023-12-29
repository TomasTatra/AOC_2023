import java.io.File
import java.io.InputStream
import java.lang.IndexOutOfBoundsException

const val movements = 3 * 131

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

    var moves = mutableMapOf(Pair(0, 0) to mutableSetOf(start))
    val mainMoves = mutableListOf<ULong>()
    val that = Pair(0, 0)

    for (i in 0..<movements) {
        val temp = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()
        for (map in moves.keys) {
            val outputMove = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()
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
            for (j in outputMove.keys) {
                val position = Pair(map.first + j.first, map.second + j.second)
                if (position !in temp.keys)
                    temp[position] = mutableSetOf()
                temp[position]!!.addAll(outputMove[j]!!)
            }
        }
        moves = temp
        if ((i + 1) % 131 == 65)
            mainMoves.add(moves.values.sumOf { e -> e.count().toULong() })

    }
    println(count((26501365 / 131).toULong(), mainMoves))
}

fun count(cons: ULong, moves: MutableList<ULong>): ULong {
    return moves[0] + cons * (moves[1] - moves[0] + (cons - 1u) * (moves[2] - moves[1] - moves[1] + moves[0]) / 2u)
}

