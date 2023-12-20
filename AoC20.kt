import java.io.File
import java.io.InputStream

class Machine(nameI: String, typeI: String, outputI: List<String>) {
    val name = nameI
    private val type = typeI
    private var on = false
    val output = outputI
    private var lastPulse = mutableMapOf<String, Boolean>()

    fun put(name: String) {
        lastPulse[name] = false
    }

    fun handlePulse(by: String, pulse: Boolean) {
        val pulseOut: Boolean
        when (type) {
            "%" -> {
                if (pulse) return
                on = !on
                pulseOut = on
            }

            "&" -> {
                lastPulse[by] = pulse
                pulseOut = lastPulse.values.contains(false)
            }

            else -> { //broadcast
                pulseOut = pulse
            }
        }
        for (machine in output) {
            pulses.add(Pair(machine, Pair(name, pulseOut)))
        }
    }

    fun reset() {
        on = false
        for (machine in lastPulse.keys) {
            lastPulse[machine] = false
        }
    }
}

val dictionary = mutableMapOf<String, Machine>()
val pulses = mutableListOf<Pair<String, Pair<String, Boolean>>>()

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    val input = inputStream.bufferedReader().use { it.readText().split("\r\n") }

    val conjunction = mutableListOf<String>()
    for (row in input) {
        val split = row.split(" -> ")
        val name = if (split[0] == "broadcaster") split[0] else split[0].substring(1, split[0].length)
        val machines = split[1].split(", ")
        val machine = Machine(name, split[0][0].toString(), machines)
        if (split[0][0].toString() == "&")
            conjunction.add(name)
        dictionary[name] = machine
    }
    for (machine in dictionary.keys) {
        for (output in dictionary[machine]!!.output) {
            if (output in conjunction) {
                for (con in conjunction) {
                    if (con == output) {
                        dictionary[con]!!.put(dictionary[machine]!!.name)
                    }
                }
            }
        }
    }

    var find = setOf("rx")
    for (i in 0..1) {
        val newFind = mutableSetOf<String>()
        for (machine in dictionary.keys) {
            for (output in dictionary[machine]!!.output) {
                if (output in find) {
                    for (search in find) {
                        if (search == output)
                            newFind.add(dictionary[machine]!!.name)
                    }
                }
            }
        }
        find = newFind
    }
    calculate()
    val findNumber = mutableListOf<Int>()
    for (search in find) {
        for (i in dictionary.values) {
            i.reset()
        }
        pulses.clear()
        findNumber.add(calculateForever(search))
    }
    println(lcm(findNumber))
}

fun lcm(findNumber: MutableList<Int>): ULong {
    val numbers = mutableListOf<Int>()
    var i = 2
    while (findNumber.count { e -> e == 1 } != findNumber.count()) {
        var decreased = false
        for (index in findNumber.indices) {
            if (findNumber[index] % i == 0) {
                decreased = true
                findNumber[index] /= i
            }
        }
        if (decreased) numbers.add(i)
        else i++
    }
    var sum: ULong = (1).toULong()
    for (number in numbers) sum *= number.toULong()
    return sum
}

fun calculate() {
    var sumT = 0
    var sumF = 0
    for (i in 0..<1000) {
        pulses.add(Pair("broadcaster", Pair("button", false)))
        while (pulses.isNotEmpty()) {
            val toDo = pulses.removeAt(0)
            if (dictionary.containsKey(toDo.first))
                dictionary[toDo.first]!!.handlePulse(toDo.second.first, toDo.second.second)
            if (toDo.second.second)
                sumT++
            else
                sumF++
        }
    }
    println("$sumT, $sumF, ${sumT * sumF}")
}

fun calculateForever(find: String): Int {
    var i = 1
    while (true) {
        pulses.add(Pair("broadcaster", Pair("button", false)))
        while (pulses.isNotEmpty()) {
            val toDo = pulses.removeAt(0)
            if (toDo.first == find && !toDo.second.second) {
                return i
            }
            if (dictionary.containsKey(toDo.first))
                dictionary[toDo.first]!!.handlePulse(toDo.second.first, toDo.second.second)
        }
        i++
    }
}

