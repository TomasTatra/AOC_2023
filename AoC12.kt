import java.io.File
import java.io.InputStream

val map = hashMapOf<Pair<String, List<Int>>, Long>()

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n") }
    var sum = (0).toULong()
    for (row in stringArray) {
        val splitRow = row.split(" ")
        var left = splitRow[0]
        val right = splitRow[1].split(",").map { e -> e.toInt() }.toMutableList()
        for (i in 0..<4) {
            left += "?" + splitRow[0]
            right.addAll(splitRow[1].split(",").map { e -> e.toInt() })
        }
        val value = backtrackingString(left, right)
        sum += value.toULong()
    }
    println(sum)
}

private fun remove(left: String): String {
    var left1 = left
    while (left1.contains(".."))
        left1 = left1.replace("..", ".")
    while (left1.isNotEmpty() && left1.first() == '.')
        left1 = left1.removePrefix(".")
    while (left1.isNotEmpty() && left1.last() == '.')
        left1 = left1.removeSuffix(".")
    return left1
}

private fun backtrackingString(left: String, right: List<Int>): Long {
    var sum = (0).toLong()
    if (left.count { e -> e == '?' } == 0) {
        return if (validString(left, right)) (1).toLong() else (0).toLong()
    }
    val end = endedResult(left, right)
    if (end == Pair("e", listOf(-1)))
        return (0).toLong()
    if (map.contains(end) && left[left.indexOf("?")-1] != '#')
        return map.getValue(end).toLong()
    if (left.count { e -> e == '#' } < right.sum()) {
        sum += if (map.contains(end) && left[left.indexOf("?")-1] != '#')
            map.getValue(end).toLong()
        else
            backtrackingString(left.replaceFirst("?", "#"), right)
    }
    if (left.count { e -> e == '.' } < left.length - right.sum()){
        sum += if (map.contains(end) && left[left.indexOf("?")-1] != '#')
            map.getValue(end).toLong()
        else
            backtrackingString(left.replaceFirst("?", "."), right)
    }
    if (!map.containsKey(end) && end != Pair("", listOf<Long>()) &&
        (left.replace("#?", "1").indexOf("1") > left.indexOfFirst { e -> e == '?' }
                || left.replace("#?", "1").indexOf("1") == -1)) {
        map[end] = sum
    }
    return sum
}

private fun endedResult(left: String, right: List<Int>): Pair<String, List<Int>> {
    val leftS = left.split("?")[0]
    val leftStart = remove(leftS).split(".")
    val rightEnd = right.toMutableList()
    for (index in leftStart.indices) {
        if (leftStart[index].length == right[index])
            rightEnd.removeAt(0)
        else if (leftStart[index].length < right[index] && leftStart.lastIndex == index && (left.first() == '.' || leftS.isEmpty() || leftS.contains("#")))
            return Pair("", listOf())
        else
            return Pair("e", listOf(-1))
    }
    return Pair(left.subSequence(left.indexOfFirst { e -> e == '?' }, left.length).toString(), rightEnd)
}

private fun validString(left: String, right: List<Int>): Boolean {
    val leftArray = remove(left).split(".")
    if (leftArray.size != right.size)
        return false
    for (index in right.indices) {
        if (leftArray[index].length != right[index])
            return false
    }
    return true
}

