package day07

import kotlinx.coroutines.*
import utils.*
import kotlin.time.measureTimedValue

interface Op : (Long, Long) -> Long

object Add : Op {
    override fun invoke(p1: Long, p2: Long): Long = p1 + p2
}

object Mul : Op {
    override fun invoke(p1: Long, p2: Long): Long = p1 * p2
}

object Join : Op {
    override fun invoke(p1: Long, p2: Long): Long = "$p1$p2".toLong()
}


fun main() {

    data class Equation(val res: Long, val numbers: List<Long>)

    fun String.toEquation() = Equation(
        substringBefore(':').toLong(),
        substringAfter(": ").split(' ').map { it.trim().toLong() },
    )

    fun testEquation(res: Long, currentRes: Long, numbers: List<Long>, op: Op, opVariants: List<Op>): Boolean {
        if (currentRes > res) return false
        if (numbers.isEmpty()) return false

        val opResult = op(currentRes, numbers.first())
        if (opResult == res) {
            return true
        }
        return opVariants.any {
            testEquation(res, opResult, numbers.drop(1), it, opVariants)
        }
    }

    fun solve(lines: List<String>, opVariants: List<Op>) = runBlocking {
        val equations = lines.map { line -> line.toEquation() }
        equations.sumOf { equation ->
            if (testEquation(equation.res, 0, equation.numbers, Add, opVariants)) {
                equation.res
            } else {
                0
            }
        }
    }

    // correct for test_input, but not for real data :-(
    val input = readTestInput("day07/test_input")
//    val input = readInput("Day07")

    listOf(
        listOf(Add, Mul),
        listOf(Add, Mul, Join)
    ).forEach { opVariants ->
        val (result, duration) = measureTimedValue {
            solve(input, opVariants)
        }
        println("$result (${duration.inWholeMilliseconds} ms)")
    }
}