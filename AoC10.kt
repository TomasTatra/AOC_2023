import java.io.File
import java.io.InputStream

val F_L = listOf('-', 'F', 'L')
val J_7 = listOf('-', 'J', '7')


fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val matrix = inputStream.bufferedReader().use { it.readText().split("\r\n") }
    val newMap = matrix.map { e -> e.map { _ -> '.' }.toMutableList() }.toMutableList()

    var start = Pair(-1, -1)

    for (x in matrix.indices) {
        val y = matrix[x].indexOf('S')
        if (y != -1) {
            start = Pair(x, y)
            break
        }
    }

    var length = 0
    var last = start
    var now = start
    while (true) {
        val actual = matrix[now.first][now.second]
        if (actual == 'S' && length != 0)
            break
        length++
        var next: Char
        var new = Pair(now.first - 1, now.second)
        if (now.first > 0 && new != last) {
            next = matrix[now.first - 1][now.second]
            if (actual in listOf('S', '|', 'J', 'L') && next in listOf('S', '|', 'F', '7')) {
                newMap[new.first][new.second] = next
                last = now
                now = new
                continue
            }
        }
        new = Pair(now.first + 1, now.second)
        if (now.first < matrix.size - 1 && new != last) {
            next = matrix[now.first + 1][now.second]
            if (actual in listOf('S', '|', '7', 'F') && next in listOf('S', '|', 'J', 'L')) {
                newMap[new.first][new.second] = next
                last = now
                now = new
                continue
            }
        }
        new = Pair(now.first, now.second - 1)
        if (now.second > 0 && new != last) {
            next = matrix[now.first][now.second - 1]
            if (actual in listOf('S', '-', 'J', '7') && next in listOf('S', '-', 'F', 'L')) {
                newMap[new.first][new.second] = next
                last = now
                now = new
                continue
            }
        }
        new = Pair(now.first, now.second + 1)
        if (now.second < matrix[now.first].length - 1 && new != last) {
            next = matrix[now.first][now.second + 1]
            if (actual in listOf('S', '-', 'F', 'L') && next in listOf('S', '-', 'J', '7')) {
                newMap[new.first][new.second] = next
                last = now
                now = new
                continue
            }
        }
        break
    }
    newMap[start.first][start.second] = replace(start, newMap, matrix)

    var count = 0
    for (xx in newMap.indices) {
        val found = mutableListOf<Char>()
        var inside = false
        val x = newMap[xx]
        for (yy in x.indices) {
            val y = x[yy]
            if (y == '.' && inside)
                count++
            if (y in listOf('7', 'F', 'L', 'J', '|')) {
                if (y == '|') {
                    if (found.isNotEmpty() && found.last() == '|')
                        found.removeAt(found.lastIndex)
                    else if (found.size > 1 && (found.takeLast(2) == listOf(
                            'F',
                            'J'
                        ) || found.takeLast(2) == listOf('L', '7'))
                    ) {
                        found.removeAt(found.lastIndex)
                        found.removeAt(found.lastIndex)
                    } else
                        found.add(y)
                }
                if (y == 'F') {
                    if (found.isNotEmpty() && found.last() == '7')
                        found.removeAt(found.lastIndex)
                    else
                        found.add(y)
                }
                if (y == '7') {
                    if (found.isNotEmpty() && found.last() == 'F')
                        found.removeAt(found.lastIndex)
                    else if (found.size > 1 && (found.takeLast(2) == listOf('|', 'L'))) {
                        found.removeAt(found.lastIndex)
                        found.removeAt(found.lastIndex)
                    } else
                        found.add(y)
                }
                if (y == 'J') {
                    if (found.isNotEmpty() && found.last() == 'L')
                        found.removeAt(found.lastIndex)
                    else if (found.size > 1 && (found.takeLast(2) == listOf('|', 'F'))) {
                        found.removeAt(found.lastIndex)
                        found.removeAt(found.lastIndex)
                    } else
                        found.add(y)
                }
                if (y == 'L') {
                    if (found.isNotEmpty() && found.last() == 'J')
                        found.removeAt(found.lastIndex)
                    else
                        found.add(y)
                }
                if (found.size > 3 && (found.takeLast(4) == listOf('L', '7', 'L', '7') ||
                            found.takeLast(4) == listOf('F', 'J', 'F', 'J'))) {
                    found.removeAt(found.lastIndex)
                    found.removeAt(found.lastIndex)
                    found.removeAt(found.lastIndex)
                    found.removeAt(found.lastIndex)
                }
                inside = found.isNotEmpty()
            }
        }
    }
    println(count)
}

private fun replace(start: Pair<Int, Int>, newMap: MutableList<MutableList<Char>>, matrix: List<String>): Char {
    if (start.first > 0 && newMap[start.first - 1][start.second] in listOf('|', 'F', '7')) {
        if (start.first < matrix.size - 1 && newMap[start.first + 1][start.second] in listOf('|', 'J', 'L')) {
            return '|'
        } else if (start.second > 0 && newMap[start.first][start.second - 1] in F_L) {
            return 'J'
        } else if (start.second < matrix[start.first].length - 1 && newMap[start.first][start.second + 1] in J_7
        ) {
            return 'L'
        }
    }

    if (start.first < matrix.size - 1 && newMap[start.first + 1][start.second] in listOf('|', 'J', 'L')) {
        if (start.second > 0 && newMap[start.first][start.second - 1] in F_L) {
            return '7'
        } else if (start.second < matrix[start.first].length - 1 && newMap[start.first][start.second + 1] in J_7
        ) {
            return 'F'
        }
    }

    if (start.second > 0 && newMap[start.first][start.second - 1] in F_L) {
        if (start.second < matrix[start.first].length - 1 && newMap[start.first][start.second + 1] in J_7
        ) {
            return '-'
        }
    }

    println("Wrong")
    return '8'
}
