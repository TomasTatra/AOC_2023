import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    var stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n") }

    val visited = mutableMapOf<List<String>,Long>()
    val target = 4 * (1000000000).toLong()
    var k:Long = 0
    while (k < target) {
        if (k % 4 == (0).toLong()) {
            if (stringArray in visited){
                val cycle = k - visited[stringArray]!!
                val value = (target - k)/cycle
                k += value * cycle
            }
            visited[stringArray] = k
        }
        val rotated = if (k == (0).toLong())
            transform(stringArray).toMutableList()
        else
            transform2(stringArray).toMutableList()
        for (rowIndex in rotated.indices) {
            val row = rotated[rowIndex]
            val stones = row.split("#")
            val output = mutableListOf<String>()
            for (j in stones.indices) {
                output.add(stones[j].toCharArray().sortedDescending().joinToString(""))
            }
            rotated[rowIndex] = output.joinToString("#")
        }
        stringArray = rotated.toList()
        k++
    }
    var total = 0
    val rotated = transform2(stringArray).toMutableList()
    for (rowIndex in rotated.indices) {
        val row = rotated[rowIndex]
        var sum = 0
        for (i in row.indices) {
            if (row[i] == 'O')
                sum += rotated.size - i
        }
        total += sum
        stringArray = rotated
    }
    println("Total $total")
    for (i in transform2(stringArray))
        println(i)
}


private fun transform(input: List<String>): List<String> {
    val output = mutableListOf<String>()
    for (i in input[0].indices) {
        output.add("")
        for (j in input.indices) {
            output[i] = output[i] + input[j][input.lastIndex - i]
        }
    }
    return output.toList()
}

private fun transform2(input: List<String>): List<String> {
    val output = mutableListOf<String>()
    for (i in input[0].indices) {
        output.add("")
        for (j in input.indices) {
            output[i] = output[i] + input[input.lastIndex - j][i]
        }
    }
    return output.toList()
}