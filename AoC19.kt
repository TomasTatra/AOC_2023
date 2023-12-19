import java.io.File
import java.io.InputStream

private val graph = mutableMapOf<String, MutableList<Rule>>()
private var totalSum:ULong = (0).toULong()

private class Rule(variableI: String, operandI: Int, valueI: Int, nextI: String) {
    val variable = variableI
    val operand = operandI
    val value = valueI
    val next = nextI
}

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val input = inputStream.bufferedReader().use { it.readText().split("\r\n\r\n") }

    for (row in input[0].split("\r\n")) {
        var split = row.trim().split("{")
        val name = split[0]
        split = split[1].trim().subSequence(0, split[1].length - 1).toString().split(",")

        for (rules in split) {
            if (!rules.contains(":")) {
                graph[name]!!.add(Rule("", 0, 0, rules))
                continue
            }
            val rule = rules.split(":")[0]
            val next = rules.split(":")[1]
            val variable = rule[0].toString()
            val operand = if (rule[1] == '<') 0 else 1
            val value = rule.subSequence(2, rule.length).toString().toInt()
            if (!graph.containsKey(name))
                graph[name] = mutableListOf(Rule(variable, operand, value, next))
            else
                graph[name]!!.add(Rule(variable, operand, value, next))
        }
    }

    var sum = 0
    for (row in input[1].split("\r\n")) {
        val map = mutableMapOf<String, Int>()
        val split = row.subSequence(1, row.length - 1).toString().split(",")
        for (definition in split) {
            map[definition.split("=")[0]] = definition.split("=")[1].toInt()
        }
        if (accepted(map, "in"))
            sum += map.values.sum()
    }
    val mapRange = mutableMapOf(
        "x" to IntRange(1, 4000),
        "m" to IntRange(1, 4000),
        "a" to IntRange(1, 4000),
        "s" to IntRange(1, 4000)
    )
    acceptedRange(mapRange, "in")

    println("part1: $sum")
    println("part2: $totalSum")
}

fun acceptedRange(mapRange: MutableMap<String, IntRange>, name: String) {
    if (name == "R")
        return
    if (name == "A") {
        var number:ULong = (1).toULong()
        for (i in mapRange)
            number *= (i.value.last - i.value.first + 1).toULong()
        totalSum += number
        return
    }
    for (i in graph[name]!!) {
        if (i.variable == ""){
            acceptedRange(mapRange, i.next)
            continue
        }
        val mapRange1 = mapRange.toMap().toMutableMap()
        if (i.operand == 1){
            mapRange1[i.variable] = IntRange(i.value+1, mapRange[i.variable]!!.last)
            mapRange[i.variable] = IntRange(mapRange[i.variable]!!.first, i.value)
            acceptedRange(mapRange1, i.next)
        } else {
            mapRange1[i.variable] = IntRange(mapRange[i.variable]!!.first, i.value-1)
            mapRange[i.variable] = IntRange(i.value, mapRange[i.variable]!!.last)
            acceptedRange(mapRange1, i.next)
        }
    }
}

fun accepted(map: MutableMap<String, Int>, name: String): Boolean {
    if (name == "A")
        return true
    if (name == "R")
        return false
    for (i in graph[name]!!) {
        if (i.variable == "")
            return accepted(map, i.next)
        if (map[i.variable]!! > i.value && i.operand == 1)
            return accepted(map, i.next)
        if (map[i.variable]!! < i.value && i.operand == 0)
            return accepted(map, i.next)
    }
    return false
}
