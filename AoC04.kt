import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\n") }

    val sumPoints = mutableListOf<Int>()
    for (i in stringArray.indices)
        sumPoints.add(1)

    for ((index, i) in stringArray.withIndex()) {
        val row = i.subSequence(i.indexOf(":") + 1, i.length).toString()
        val input = row.split("|")[0].trim().split(" ").sorted()
        val your = row.split("|")[1].trim().split(" ").sorted()
        var inputIndex = 0
        var points = 0
        for (number in your) {
            while (inputIndex < input.size && number > input[inputIndex])
                inputIndex++
            if (inputIndex < input.size && number == input[inputIndex] && number != "") {
                points++
            }
        }
        for (point in 0..<points) {
            sumPoints[index + 1 + point] += sumPoints[index]
        }
    }
    println(sumPoints.sum())
}
