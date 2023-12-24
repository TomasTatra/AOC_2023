import java.io.File

val slopes = mapOf("v" to Pair(1, 0), "^" to Pair(-1, 0), ">" to Pair(0, 1), "<" to Pair(0, -1))
val simpleMap = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Pair<Int, Int>, List<Pair<Int, Int>>>>>()
val directions = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
var end = Pair(0, 0)

fun main() {
    val inputStream = File("example.txt").inputStream()
    val input = inputStream.bufferedReader()
        .use { it.readText().split("\r\n").map { e -> e.split("").subList(1, e.length + 1) } }
    val moves = mutableListOf(Pair(Pair(0, 1), mutableListOf(Pair(0, 1), Pair(1, 1))))
    end = Pair(input.lastIndex, input[input.lastIndex].lastIndex - 1)
    val start = Pair(0, 1)

    while (moves.isNotEmpty()) {
        var actualPath = moves.removeAt(0)
        val actualMove = actualPath.second.last()
        if (actualMove == start) {
            continue
        }
        if (actualMove == end || between(actualMove, input)) {
            val temp = actualPath.second
            temp.removeAt(temp.lastIndex)
            if (!simpleMap.keys.contains(actualPath.first))
                simpleMap[actualPath.first] = mutableSetOf()
            simpleMap[actualPath.first]!!.add(Pair(actualMove, temp))
            if (actualMove == end)
                continue
            actualPath = Pair(actualMove, mutableListOf(actualMove))
        }
        for (direction in directions) {
            val newMove = Pair(actualMove.first + direction.first, actualMove.second + direction.second)
            if (newMove in actualPath.second)
                continue
            var find = false
            if (actualPath.first in simpleMap.keys) {
                for (startMove in simpleMap[actualPath.first]!!) {
                    if (startMove.second.contains(newMove))
                        find = true
                }
            }
            if (find)
                continue
            val nextTile = input[newMove.first][newMove.second]
            if (nextTile == "#")
                continue
            val newPath = actualPath.second.toMutableList()
            newPath.add(newMove)
            moves.add(Pair(actualPath.first, newPath))

        }
    }
    println(backtrack(mutableListOf(Pair(0, 1)), 0))
}

fun backtrack(path: MutableList<Pair<Int, Int>>, sum: Int): Int {
    if (path.last() == end) {
        return sum
    }
    var max = 0
    for (next in simpleMap[path.last()]!!) {
        if (path.contains(next.first))
            continue
        val copyPath = path.toMutableList()
        copyPath.add(next.first)
        val copySum = sum + next.second.size
        val actual = backtrack(copyPath, copySum)
        if (actual > max)
            max = actual
    }
    return max
}

fun count(actual: MutableList<Pair<Int, Int>>): Long {
    var sum: Long = 0
    for (pointIndex in 0..<actual.size - 1) {
        for (pointNext in simpleMap[actual[pointIndex]]!!.toList().sortedBy { e -> e.second.size }.reversed()) {
            if (pointNext.first == actual[pointIndex + 1]) {
                sum += pointNext.second.size
                break
            }
        }
    }
    return sum
}

fun between(move: Pair<Int, Int>, input: List<List<String>>): Boolean {
    var count = 0
    for (direction in directions) {
        val nextMove = Pair(move.first + direction.first, move.second + direction.second)
        if (input[nextMove.first][nextMove.second] in slopes.keys)
            count++
    }
    return count > 2

}