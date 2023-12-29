import java.io.File
import java.io.InputStream
import java.lang.IndexOutOfBoundsException

private val direction = listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val strings = inputStream.bufferedReader().use { it.readText().split("\r\n") }
    val map = strings.map { e -> e.map { k -> k.digitToInt() } }
    val graph = strings.map { e -> e.map { _ -> mutableListOf<MutableList<Int>>() }.toMutableList() }.toMutableList()
    graph[0][0].add(listOf(0, 0, 0, 0).toMutableList())
    val input = mutableListOf(Pair(0, 0))
    val end = Pair(map.lastIndex, map.last().lastIndex)
    while (input.isNotEmpty()) {
        val position = input.removeAt(0)
        if (position == end)
            continue
        for (way in direction) {
            val newPosition = Pair(position.first + way.first, position.second + way.second)
            val value: Int
            try {
                value = map[newPosition.first][newPosition.second]
            } catch (_: IndexOutOfBoundsException) {
                continue
            }
            for (set in graph[position.first][position.second]) {
                val oldValue = set[0]
                val last = Pair(set[1], set[2])
                val oneWay = set[3]
                if (oneWay > 10)
                    continue
                if (last != Pair(0, 0))
                    if (oneWay < 4 && last != way)
                        continue
                if (last.first * -1 == way.first && last.second * -1 == way.second)
                    continue
                val newValue = oldValue + value
                var find = false
                val copy = graph[newPosition.first][newPosition.second]
                for (secondSet in copy) {
                    val secondValue = secondSet[0]
                    val secondLast = Pair(secondSet[1], secondSet[2])
                    val secondOneWay = secondSet[3]
                    if (last == way) {
                        if (secondLast == way && secondOneWay == oneWay + 1) {
                            if (secondValue > newValue) {
                                graph[newPosition.first][newPosition.second].remove(secondSet)
                                graph[newPosition.first][newPosition.second].add(
                                    mutableListOf(
                                        newValue, way.first, way.second, secondOneWay
                                    )
                                )
                                if (secondOneWay > 3 && !input.contains(newPosition))
                                    input.add(newPosition)
                            }
                            find = true
                            break
                        }
                    } else {
                        if (secondLast == way && secondOneWay == 1) {
                            if (secondValue > newValue) {
                                graph[newPosition.first][newPosition.second].remove(secondSet)
                                graph[newPosition.first][newPosition.second].add(
                                    mutableListOf(
                                        newValue, way.first, way.second, 1
                                    )
                                )
                                if (!input.contains(newPosition))
                                    input.add(newPosition)
                            }
                            find = true
                            break
                        }
                    }
                }
                if (!find) {
                    if (!input.contains(newPosition))
                        input.add(newPosition)
                    if (last == way) {
                        if (oneWay < 10)
                            graph[newPosition.first][newPosition.second].add(
                                mutableListOf(
                                    newValue, way.first, way.second, oneWay + 1
                                )
                            )
                    } else
                        graph[newPosition.first][newPosition.second].add(
                            mutableListOf(
                                newValue, way.first, way.second, 1
                            )
                        )
                }
            }
        }
    }
    var min = Int.MAX_VALUE
    for (m in graph[end.first][end.second]) {
        if (m[0] < min && m[3] > 3)
            min = m[0]
    }
    println(min)
}