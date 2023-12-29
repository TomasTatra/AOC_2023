import java.io.File

class Component(nameI: String) {
    private val name = nameI
    private val neighbours = mutableListOf<Component>()

    fun add(second: Component) {
        if (neighbours.contains(second))
            return
        neighbours.add(second)
        second.add(this)
    }

    fun remove(second: Component) {
        if (!neighbours.contains(second))
            return
        neighbours.remove(second)
        second.remove(this)
    }

    fun getName(): String {
        return name
    }

    fun getNeighbours(): MutableList<Component> {
        return neighbours
    }
}

val components = mutableListOf<Component>()
val vertex = mutableListOf<Pair<String, String>>()

fun main() {
    val inputStream = File("example.txt").inputStream()
    val input = inputStream.bufferedReader()
        .use { it.readText().split("\r\n").map { e -> e.split(": ") } }
    for (row in input) {
        val first: Component =
            if (components.count { e -> e.getName() == row[0] } == 0) Component(row[0])
            else components.find { e -> e.getName() == row[0] }!!

        for (string in row[1].split(" ")) {
            val second: Component =
                if (components.count { e -> e.getName() == string } == 0) Component(string)
                else components.find { e -> e.getName() == string }!!
            first.add(second)
            vertex.add(Pair(row[0], string))
            if (components.count { e -> e.getName() == string } > 0)
                continue
            components.add(second)
        }
        if (components.count { e -> e.getName() == row[0] } > 0)
            continue
        components.add(first)
    }
    for (index1 in vertex.indices) {
        removeVertex(vertex[index1])
        for (index2 in index1 + 1..<vertex.size) {
            removeVertex(vertex[index2])
            for (index3 in index2 + 1..<vertex.size) {
                removeVertex(vertex[index3])
                val find = backtrack()
                if (find < components.count()){
                    println(find * (components.count() - find))
                    return
                }
                addVertex(vertex[index3])
            }
            addVertex(vertex[index2])
        }
        addVertex(vertex[index1])
    }
}

fun backtrack(): Int {
    val componentList = findAll(mutableListOf(components[0]))
    return componentList.count()
}

fun findAll(newComponent: MutableList<Component>): List<Component> {
    val visitedComponent = mutableSetOf<Component>()
    val addedComponent = mutableSetOf<Component>()
    while (newComponent.isNotEmpty()){
        val actualComponent = newComponent.removeAt(0)
        if (actualComponent in visitedComponent)
            continue
        visitedComponent.add(actualComponent)
        for (comp in actualComponent.getNeighbours()){
            if (comp in addedComponent)
                continue
            newComponent.add(comp)
            addedComponent.add(comp)
        }
    }
    return visitedComponent.toList()
}

fun removeVertex(vertex: Pair<String, String>) {
    val first = components.find { e -> e.getName() == vertex.first }
    val second = components.find { e -> e.getName() == vertex.second }
    first!!.remove(second!!)
}

fun addVertex(vertex: Pair<String, String>) {
    val first = components.find { e -> e.getName() == vertex.first }
    val second = components.find { e -> e.getName() == vertex.second }
    first!!.add(second!!)
}