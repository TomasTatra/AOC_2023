import java.io.File
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n") }

    val map = stringArray.map { e -> e.toMutableList() }.toMutableList()

    val lengthRow = mutableListOf<Int>()
    val lengthColumn = mutableListOf<Int>()
    val positions = mutableMapOf<Int, Pair<Int, Int>>()
    var count = 0
    for (rowIndex in map.indices) {
        for (columnIndex in map[rowIndex].indices)
            if (map[rowIndex][columnIndex] == '#') {
                positions[count] = Pair(rowIndex, columnIndex)
                count++
            }
        if (map[rowIndex].count { e -> e == '#' } == 0)
            lengthRow.add(1000000)
        else
            lengthRow.add(1)
        if (map.count { e -> e[rowIndex] == '#' } == 0)
            lengthColumn.add(1000000)
        else
            lengthColumn.add(1)
    }

    var sum = (0).toLong()
    for (i in 0..<positions.size) {
        for (j in i + 1..<positions.size) {
            for (position in positions[i]!!.first + 1..positions[j]!!.first)
                sum += lengthRow[position]
            for (position in min(positions[i]!!.second, positions[j]!!.second) + 1..max(
                positions[i]!!.second,
                positions[j]!!.second
            ))
                sum += lengthColumn[position]
        }
    }
    println(sum)
}
