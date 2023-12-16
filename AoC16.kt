import java.io.File
import java.io.InputStream
import kotlin.math.abs

val mirror = listOf("\\", "/")
val splitter = listOf("|", "-")

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n") }

    val matrix = mutableListOf<List<String>>()
    for (row in stringArray)
        matrix.add(row.split("").subList(1, row.length + 1))

    var max = 0
    for (rowIndex in matrix.indices) {
        var input = mutableListOf(Pair(Pair(rowIndex, 0), Pair(0, 1)))
        var sum = findValue(input, matrix)
        if (sum > max)
            max = sum
        input = mutableListOf(Pair(Pair(rowIndex, matrix[0].lastIndex), Pair(0, -1)))
        sum = findValue(input, matrix)
        if (sum > max)
            max = sum
    }
    for (columnIndex in matrix[0].indices) {
        var input = mutableListOf(Pair(Pair(0, columnIndex), Pair(1, 0)))
        var sum = findValue(input, matrix)
        if (sum > max)
            max = sum
        input = mutableListOf(Pair(Pair(matrix.lastIndex, columnIndex), Pair(-1, 0)))
        sum = findValue(input, matrix)
        if (sum > max)
            max = sum
    }
    println(max)
}

fun findValue(input: MutableList<Pair<Pair<Int, Int>, Pair<Int, Int>>>, matrix: MutableList<List<String>>): Int {
    val energized =
        matrix.map { e -> e.map { _ -> Pair(0, mutableSetOf<Pair<Int, Int>>()) }.toMutableList() }.toMutableList()
    var start = Pair(-1, -1)
    var vector = Pair(0, 2)
    while (vector.first != vector.second) {
        var actual: String
        try {
            actual = matrix[start.first][start.second]
        } catch (_: IndexOutOfBoundsException) {
            if (input.isEmpty()) {
                vector = Pair(0, 0)
                continue
            } else
                vector = input[0].second
            start = input[0].first
            input.removeAt(0)
            continue
        }
        if (energized[start.first][start.second].second.contains(vector)) {
            start = Pair(-1, -1)
            continue
        } else {
            energized[start.first][start.second] = Pair(1, energized[start.first][start.second].second)
            energized[start.first][start.second].second.add(vector)

        }
        if (actual in mirror) {
            vector = if (actual == "/")
                Pair(vector.second * -1, vector.first * -1)
            else
                Pair(vector.second, vector.first)
        } else if (actual in splitter) {
            if (abs(vector.first) == 1 && actual == "-") {
                vector = Pair(vector.second, vector.first)
                input.add(
                    Pair(
                        Pair(start.first - vector.first, start.second - vector.second),
                        Pair(vector.first, -vector.second)
                    )
                )
            }
            if (abs(vector.second) == 1 && actual == "|") {
                vector = Pair(vector.second, vector.first)
                input.add(
                    Pair(
                        Pair(start.first - vector.first, start.second - vector.second),
                        Pair(-vector.first, vector.second)
                    )
                )
            }
        }
        start = Pair(start.first + vector.first, start.second + vector.second)
    }

    var sum = 0
    for (row in energized)
        for (column in row)
            if (column.first > 0)
                sum++
    return sum
}
