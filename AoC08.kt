import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n") }

    val instruction = stringArray[0]
    val map = mutableMapOf<String, Pair<String, String>>()
    val positions = mutableListOf<String>()

    for (i in 2..<stringArray.size) {
        val row = stringArray[i].split(" = ")
        val destination = row[1].split(", ")
        map[row[0]] = Pair(
            destination[0].substring(1, destination[0].length),
            destination[1].substring(0, destination[1].length - 1)
        )
        if (row[0].last() == 'A')
            positions.add(row[0])
    }
    val steps = mutableListOf<Int>()
    for (position in positions) {
        var step = 0
        var temp = position
        while (temp.last() != 'Z') {
            val turn = instruction[step % instruction.length]
            if (turn == 'L')
                temp = map[temp]!!.first
            if (turn == 'R')
                temp = map[temp]!!.second
            step++
        }
        steps.add(step)
    }
    val min = steps.max().toLong()
    var actual = min
    while (notEnd(steps, actual)) {
        actual += min
    }
    println(actual)
}

private fun notEnd(steps: MutableList<Int>, actual: Long): Boolean {
    for (i in steps) {
        if (actual % i != (0).toLong())
            return true
    }
    return false
}