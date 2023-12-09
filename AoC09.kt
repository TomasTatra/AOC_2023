import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n") }
    var sum = (0).toLong()
    for (row in stringArray) {
        val input = row.trim().split(" ").map { e -> e.toLong() }
        sum += backtrackingBackward(input)
    }
    println(sum)
}

fun backtracking(input: List<Long>): Long {
    val numbers = mutableListOf<Long>()
    for (index in 0..<input.size - 1) {
        numbers.add(input[index + 1] - input[index])
    }
    if (numbers.toSet().count() == 1)
        return input.last() + numbers[0]
    return input.last() + backtracking(numbers)
}

fun backtrackingBackward(input: List<Long>): Long {
    val numbers = mutableListOf<Long>()
    for (index in 0..<input.size - 1) {
        numbers.add(input[index + 1] - input[index])
    }
    if (numbers.toSet().count() == 1)
        return input.first() - numbers[0]
    return input.first() - backtrackingBackward(numbers)
}
