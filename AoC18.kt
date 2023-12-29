import java.io.File
import java.io.InputStream
import kotlin.math.abs

private val mapOfMove = mapOf(0 to Pair(0, 1), 2 to Pair(0, -1), 1 to Pair(1, 0), 3 to Pair(-1, 0))

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val strings = inputStream.bufferedReader().use { it.readText().split("\r\n") }

    val graph = mutableListOf<List<Long>>()
    var positionX:Long = 0
    var positionY:Long = 0
    var distance:Long = 0
    for (string in strings) {
        val instruction = string.split(" ")
        val color = instruction[2].subSequence(1, instruction[2].length - 1).toString()
        val direction = color.last().toString().toInt()
        val long = color.subSequence(1, 6).toString().toInt(16)
        val newPositionX:Long = positionX + mapOfMove[direction]!!.first * long
        val newPositionY:Long = positionY + mapOfMove[direction]!!.second * long
        graph.add(listOf(positionX, positionY))
        positionX = newPositionX
        positionY = newPositionY
        distance += long
    }
    println(dig(graph) + distance.shr(1) + 1)
}

private fun dig(graph: MutableList<List<Long>>): Long {
    var sum:Long = 0
    for (index in graph.indices) {
        val x1 = graph[index][0]
        val y1 = graph[index][1]
        val x2: Long
        val y2: Long
        if (index != graph.lastIndex) {
            x2 = graph[index + 1][0]
            y2 = graph[index + 1][1]
        } else {
            x2 = graph[0][0]
            y2 = graph[0][1]
        }
        sum += (x1 * y2 - x2 * y1)
    }
    return abs(sum).shr(1)
}
