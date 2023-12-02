import java.io.File
import java.io.InputStream

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\n") }

    /*val totalRed = 12
    val totalGreen = 13
    val totalBlue = 14
    var sumOfID = 0*/
    var sumOfCube = 0

    for (row in stringArray) {
        val line = row.split(": ")
        //val key = line[0].replace("Game ", "").toInt()
        val sets = line[1].split("; ")
        var maxRed = 0
        var maxGreen = 0
        var maxBlue = 0
        for (set in sets) {
            var red = 0
            var green = 0
            var blue = 0
            val list = set.split(", ")
            for (i in list) {
                val string = i.split(" ")
                if ("red" in string[1])
                    red += string[0].toInt()
                if ("green" in string[1])
                    green += string[0].toInt()
                if ("blue" in string[1])
                    blue += string[0].toInt()
            }
            if (red > maxRed)
                maxRed = red
            if (green > maxGreen)
                maxGreen = green
            if (blue > maxBlue)
                maxBlue = blue
        }
        /*if (maxRed <= totalRed && maxGreen <= totalGreen && maxBlue <= totalBlue)
            sumOfID += key*/
        sumOfCube += maxRed*maxGreen*maxBlue
    }
    println(sumOfCube)
    //println(sumOfID)
}