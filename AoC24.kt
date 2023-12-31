import java.io.File
import com.microsoft.z3.*

val range = Range(200000000000000, 400000000000000)

class Range(minI: Long, maxI: Long) {
    private val min: Double = minI.toDouble()
    private val max: Double = maxI.toDouble()

    operator fun contains(resultX: Double): Boolean {
        return resultX > min && resultX < max
    }
}

class Rock(pI: List<Long>, vI: List<Long>) {
    val pX: Double = pI[0].toDouble()
    val pY: Double = pI[1].toDouble()
    val pZ: Double = pI[2].toDouble()
    val vX: Double = vI[0].toDouble()
    val vY: Double = vI[1].toDouble()
    val vZ: Double = vI[2].toDouble()

    fun collide(rock: Rock): Boolean {
        val m1: Double = (pY + vY - pY) / (pX + vX - pX)
        val b1: Double = pY - (m1 * pX)
        val m2: Double = (rock.pY + rock.vY - rock.pY) / (rock.pX + rock.vX - rock.pX)
        val b2: Double = rock.pY - (m2 * rock.pX)
        if (m1 - m2 == 0.0)
            return b1 == b2
        val resultX: Double = (b2 - b1) / (m1 - m2)
        val resultY: Double = m1 * resultX + b1
        if (resultX > pX && vX < 0 || resultX < pX && vX > 0 )
            return false
        if (resultX > rock.pX && rock.vX < 0 || resultX < rock.pX && rock.vX > 0 )
            return false
        return (resultX in range) && (resultY in range)
    }
}

val rocks = mutableListOf<Rock>()

fun main() {
    val inputStream = File("example.txt").inputStream()
    val input = inputStream.bufferedReader()
        .use { it.readText().split("\r\n").map { e -> e.split(" @ ") } }
    for (row in input) {
        val position = row[0].split(", ").map { e -> e.trim().toLong() }
        val vector = row[1].split(", ").map { e -> e.trim().toLong() }
        rocks.add(Rock(position, vector))
    }

    var sum = 0
    for (index1 in rocks.indices) {
        for (index2 in index1 + 1..<rocks.size){
            if (rocks[index1].collide(rocks[index2])) {
                sum++
            }
        }
    }
    println("Part 1: $sum")

    val ctx = Context()
    val solver = ctx.mkSolver()
    val x = ctx.mkIntConst("x")
    val y = ctx.mkIntConst("y")
    val z = ctx.mkIntConst("z")
    val vx = ctx.mkIntConst("vx")
    val vy = ctx.mkIntConst("vy")
    val vz = ctx.mkIntConst("vz")

    for (i in 0 .. 10) { // we need min three rocks .. with more rocks it will find faster
        val h = rocks[i]
        val t = ctx.mkIntConst("t$i")
        solver.add(ctx.mkEq(ctx.mkAdd(x, ctx.mkMul(vx, t)), ctx.mkAdd(ctx.mkInt(h.pX.toLong()), ctx.mkMul(ctx.mkInt(h.vX.toLong()), t))))
        solver.add(ctx.mkEq(ctx.mkAdd(y, ctx.mkMul(vy, t)), ctx.mkAdd(ctx.mkInt(h.pY.toLong()), ctx.mkMul(ctx.mkInt(h.vY.toLong()), t))))
        solver.add(ctx.mkEq(ctx.mkAdd(z, ctx.mkMul(vz, t)), ctx.mkAdd(ctx.mkInt(h.pZ.toLong()), ctx.mkMul(ctx.mkInt(h.vZ.toLong()), t))))
    }

    print("Part 2: ")
    if (solver.check() == Status.SATISFIABLE) {
        println(solver.model.eval(ctx.mkAdd(x, ctx.mkAdd(y, z)), false))
    } else {
        println("No solution found.")
    }
}