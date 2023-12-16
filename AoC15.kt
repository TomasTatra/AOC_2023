import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val string = inputStream.bufferedReader().use { it.readText() }

    val boxes = mutableListOf<MutableList<Pair<String, Int>>>()
    for (i in 0..255)
        boxes.add(mutableListOf())
    val dict = mutableMapOf<String, Int>()
    for (word in string.split(",")) {
        if ('-' in word) {
            val name = word.replace("-", "")
            if (name !in dict)
                continue
            for (i in boxes[dict[name]!!].indices)
                if (boxes[dict[name]!!][i].first == name) {
                    boxes[dict[name]!!].removeAt(i)
                    break
                }
            dict.remove(name)
            continue
        }
        val name = word.split("=")[0]
        val box = hashName(name)
        val input = word.split("=")[1].toInt()
        if (name in dict) {
            for (i in boxes[box].indices) {
                if (boxes[box][i].first == name) {
                    boxes[box][i] = Pair(name, input)
                    break
                }
            }
            continue
        }
        boxes[box].add(Pair(name, input))
        dict[name] = box
    }

    var value: Long = 0
    for (box in boxes.indices) {
        for (element in boxes[box].indices) {
            value += (box + 1) * (element + 1) * boxes[box][element].second
        }
    }
    println(value)
}

private fun hashName(name: String): Int {
    var sum = 0
    for (char in name) {
        sum += char.code
        sum *= 17
        sum %= 256
    }
    return sum
}