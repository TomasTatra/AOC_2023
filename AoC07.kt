import java.io.File
import java.io.InputStream

private val cards = listOf("J", "2", "3", "4", "5", "6", "7", "8", "9", "T", "Q", "K", "A")
private val translate = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M")

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val stringArray = inputStream.bufferedReader().use { it.readText().split("\r\n") }

    var valueArray = mutableListOf<Pair<Int, Pair<String, Int>>>()
    for (row in stringArray) {
        val array = row.split(" ")
        valueArray.add(Pair(valueOfSecond(array[0]), Pair(array[0].map { e -> translate[cards.indexOf(e.toString())] }
            .toString(), array[1].toInt())))
    }

    valueArray = valueArray.sortedBy { e -> e.second.first }.sortedBy { e -> e.first }.toMutableList()

    var sum = 0
    var round = 1
    for (value in valueArray) {
        sum += value.second.second * round
        round++
    }
    println(sum)
}

fun valueOf(input: String): Int {
    val cards = input.split("").sorted().filter { e -> e != "" }
    if (cards.count { e -> e == cards[0] } == 5) //5
        return 5
    if (cards.count { e -> e == cards[0] } == 4 || cards.count { e -> e == cards[1] } == 4) //4
        return 4
    if ((cards.count { e -> e == cards[0] } == 3 && cards.count { e -> e == cards[3] } == 2) ||
        (cards.count { e -> e == cards[0] } == 2 && cards.count { e -> e == cards[3] } == 3)) // 3 + 2
        return 3
    if (cards.count { e -> e == cards[2] } == 3) // 3
        return 2
    if (cards.toSet().count() == 3) // 2 + 2
        return 1
    if (cards.toSet().count() == 4) // 2
        return 0
    return -1 //1
}

private fun valueOfSecond(input: String): Int {
    val value = valueOf(input)
    val countJ = input.count { e -> e.toString() == "J" }
    when (countJ) {
        0, 5 -> return value
        1 -> {
            return when (value) {
                -1 -> 0    //1234J
                0 -> 2    //1233J
                1 -> 3    //1122J
                2 -> 4    //1112J
                4 -> 5    //1111J
                else -> value
            }
        }
        2 -> {
            return when (value) {
                0 -> 2    //123JJ
                1 -> 4    //112JJ
                3 -> 5    //111JJ
                else -> value
            }
        }
        3 -> {
            return when (value) {
                2 -> 4    //12JJJ
                3 -> 5    //11JJJ
                else -> value
            }
        }
        //1JJJJ
        else -> return 5
    }
}
