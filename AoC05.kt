import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n\r\n") }
    val inputStringStart = stringArray[0].replace("seeds: ", "").split(" ").toMutableList()
    var inputStringEnd = mutableListOf<Pair<String, Long>>()
    for (i in inputStringStart.indices) {
        if (i % 2 == 1)
            continue
        inputStringEnd.add(Pair(inputStringStart[i], inputStringStart[i + 1].toLong()))
    }
    for (map in stringArray.subList(1, 8)) {
        val inputConvert = inputStringEnd.toList().toMutableList()
        val temp = mutableListOf<Pair<String, Long>>()
        while (inputConvert.isNotEmpty()) {
            val pop = inputConvert.removeAt(0)
            var found = false
            for (row in map.split("\r\n")) {
                if (row.contains("-"))
                    continue
                val mapMap = row.split(" ")
                val numberStart = pop.first.toLong()
                val numberEnd = numberStart + pop.second
                val convert = mapMap[0].toLong()
                val start = mapMap[1].toLong()
                val startLength = mapMap[2].toLong()
                val end = start + startLength
                if (numberEnd <= start || end <= numberStart)
                    continue
                found = true
                val starter = convert + numberStart - start
                if (start < numberStart) {
                    if (numberEnd <= end) {
                        temp.add(Pair(starter.toString(), numberEnd - numberStart))
                        break
                    }
                    inputConvert.add(Pair(end.toString(), pop.second - end - numberStart))
                    temp.add(Pair(starter.toString(), end - numberStart))
                    break
                }
                if (end < numberEnd) {
                    inputConvert.add(Pair(numberStart.toString(), start - numberStart))
                    temp.add(Pair(convert.toString(), startLength))
                    inputConvert.add(Pair(end.toString(), numberEnd - end))
                    break
                }
                inputConvert.add(Pair(numberStart.toString(), start - numberStart))
                temp.add(Pair(convert.toString(), numberEnd - start))
                break
            }
            if (!found)
                temp.add(pop)
        }
        inputStringEnd = temp
    }
    inputStringEnd.removeAll { e -> e.second == (0).toLong() }
    println(inputStringEnd.minOf { e -> e.first.toLong() })
}