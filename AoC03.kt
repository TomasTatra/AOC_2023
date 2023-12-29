import java.io.File
import java.io.InputStream

private val num = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
private lateinit var matrix: MutableList<MutableList<String>>

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\n") }
    val list = mutableListOf<MutableList<String>>()
    for (i in stringArray)
        list.add(i.trim().split("").subList(1, i.length).toMutableList())
    matrix = list
    var numbers = 0
    for (i in list.indices) {
        var actual = ""
        var found = 0
        for (j in matrix[i].indices) {
            if (num.contains(matrix[i][j])) {
                actual += matrix[i][j]
                if (found == 0)
                    found = find(i, j)
            } else if (actual != "") {
                if (valid(found, actual))
                    numbers += actual.toInt() * found
                actual = ""
                found = 0
            }
        }
        if (actual != "") {
            if (valid(found, actual))
                numbers += actual.toInt() * found
        }
    }
    println(numbers)
}

private fun valid(found: Int, actual: String) = found != 0 && actual.toInt() != found

private fun find(i: Int, j: Int): Int {
    for (x in i - 1..i + 1) {
        for (y in j - 1..j + 1) {
            if (x < 0 || x > matrix.size - 1 || y < 0 || y > matrix[x].size - 1)
                continue
            if (matrix[x][y] == "*") {
                val result = findNumber(x, y)
                if (result != 0)
                    return result
            }
        }
    }
    return 0
}

private fun findNumber(x: Int, y: Int): Int {
    val numbers = mutableListOf<Int>()
    for (xx in x - 1..x + 1) {
        for (yy in y - 1..y + 1) {
            if (x < 0 || x > matrix.size - 1 || y < 0 || y > matrix[x].size - 1)
                continue
            if (num.contains(matrix[xx][yy])) {
                var actual = ""
                var i = 0
                while (yy + i - 1 >= 0 && num.contains(matrix[xx][yy + i - 1]))
                    i--
                while (yy + i < matrix[xx].size && num.contains(matrix[xx][yy + i])) {
                    actual += matrix[xx][yy + i]
                    i++
                }
                if (!numbers.contains(actual.toInt()))
                    numbers.add(actual.toInt())
            }
        }
    }
    if (numbers.size == 2)
        return numbers[1]
    return 0
}
