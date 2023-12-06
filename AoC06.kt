import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    var stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n") }
    stringArray = listOf(stringArray[0].replace("Time:", "").trim(), stringArray[1].replace("Distance:", "").trim())
    val time = stringArray[0].split(" ").filter { e -> e != "" }
    val distance = stringArray[1].split(" ").filter { e -> e != "" }
    val realTime = time.joinToString("").toLong()
    val realDistance = distance.joinToString("").toLong()
    for (i in 1..<realTime) {
        if ((realTime - i) * i > realDistance) {
            println(realTime - i * 2 + 1)
            break
        }
    }
}