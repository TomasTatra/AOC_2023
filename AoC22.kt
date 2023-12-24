import java.io.File
import java.io.InputStream

class Brick(start: List<Int>, end: List<Int>) {
    private val sX = start[0]
    private val sY = start[1]
    var sZ = start[2]
    private val eX = end[0]
    private val eY = end[1]
    var eZ = end[2]

    fun fall(bricks: MutableList<Brick>) {
        val bricksGround = bricks.subList(0, bricks.indexOf(this)).asReversed()
        if (bricksGround.isEmpty())
            return
        var canMove = true
        while (canMove) {
            canMove = move(bricksGround)
            if (canMove) {
                sZ -= 1
                eZ -= 1
            }
        }
    }

    fun canFall(): Boolean {
        val bricksGround = bricks.subList(0, bricks.indexOf(this)).asReversed()
        if (bricksGround.isEmpty())
            return false
        var canMove = true
        while (canMove) {
            canMove = move(bricksGround)
            if (canMove) {
                return true
            }
        }
        return false
    }

    private fun move(bricksGround: MutableList<Brick>): Boolean {
        if (sZ == 0)
            return false
        for (brick in bricksGround) {
            if (brick.sX in IntRange(sX, eX) || sX in IntRange(brick.sX, brick.eX))
                if (brick.sY in IntRange(sY, eY) || sY in IntRange(brick.sY, brick.eY))
                    if (brick.sZ in IntRange(sZ - 1, eZ - 1) || sZ - 1 in IntRange(brick.sZ, brick.eZ))
                        return false
        }
        return true
    }

    fun copy(): Brick {
        return Brick(listOf(sX, sY, sZ), listOf(eX, eY, eZ))
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other.javaClass != this.javaClass)
            return false
        val newOther = other as Brick
        return !(sX != newOther.sX || sY != newOther.sY || sZ != newOther.sZ)
    }

    override fun hashCode(): Int {
        var result = sX
        result = 31 * result + sY
        result = 31 * result + sZ
        result = 31 * result + eX
        result = 31 * result + eY
        result = 31 * result + eZ
        return result
    }
}

val bricks = mutableListOf<Brick>()

fun main() {
    val inputStream: InputStream = File("example.txt").inputStream()
    var input = inputStream.bufferedReader()
        .use {
            it.readText().split("\r\n")
                .map { e -> e.split("~") }
        }
    input = input.sortedBy { e -> e[1].split(",").last().toInt() }
    for (row in input) {
        val start = row[0].split(",").map { e -> e.toInt() }
        val end = row[1].split(",").map { e -> e.toInt() }
        bricks.add(Brick(start, end))
        bricks.last().fall(bricks)
    }
    var x = 0
    var sum = 0
    var sum2 = 0
    while (x < bricks.size) {
        val removedBrick = bricks.removeAt(x)
        val lastZ = removedBrick.eZ
        var canRemove = true
        for (brick in bricks) {
            if (lastZ + 1 == brick.sZ)
                if (brick.canFall())
                    canRemove = false
        }
        if (canRemove)
            sum++
        if (!canRemove) {
            val newBricks = bricks.map { e -> e.copy() }.toMutableList()
            var temp = 0
            for (index in newBricks.indices) {
                newBricks[index].fall(newBricks)
                if (bricks[index] != newBricks[index])
                    temp++
            }
            sum2 += temp
            println("$x: $temp")
        }
        bricks.add(x, removedBrick)
        x++
    }
    println("Part1: $sum")
    println("Part2: $sum2")
}