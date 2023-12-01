import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val digitString = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val digitNumber = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")

    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\n") }
    var count = 0

    for (row in stringArray) {
        val replaceString1 = row.findAnyOf(digitString)
        val replaceString2 = row.findLastAnyOf(digitString)
        val replaceNumber1 = row.findAnyOf(digitNumber)
        val replaceNumber2 = row.findLastAnyOf(digitNumber)
        if (replaceString1 == null) {
            count += (replaceNumber1!!.second + replaceNumber2!!.second).toInt()
            continue
        }
        var number = ""
        if (replaceString1.first < replaceNumber1!!.first)
            number += digitNumber[digitString.indexOf(replaceString1.second)]
        else
            number += replaceNumber1.second

        if (replaceString2!!.first > replaceNumber2!!.first)
            number += digitNumber[digitString.indexOf(replaceString2.second)]
        else
            number += replaceNumber2.second

        count += number.toInt()
    }
    println(count)
}