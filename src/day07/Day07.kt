package day07

import utils.*
import kotlin.time.measureTimedValue

infix fun Long.`||`(other: Long): Long = "$this$other".toLong()

data class Equation(val res: Long, val numbers: List<Long>)

fun main() {

    fun String.toEquation() = Equation(
        substringBefore(':').toLong(),
        substringAfter(": ").split(' ').map { it.trim().toLong() }
    )

    fun solve(lines: List<String>, block: (Long, Long) -> List<Long>): Long {
        val equations = lines.map { it.toEquation() }
        val correctEquations = mutableListOf<Long>()

        equations.forEach { equation ->
            var possibleResults = listOf(equation.numbers.first())
            for ((i, num) in equation.numbers.withIndex()) {
                if (i > 0) {
                    possibleResults = possibleResults.flatMap { block(it, num) }
                }
            }
            if (possibleResults.contains(equation.res)) {
                correctEquations.add(equation.res)
            }
        }

        return correctEquations.sum()
    }


    // Test if implementation meets criteria from the description, like:
//    val testInput = readTestInput("day07/test_input")
//    println("=== TEST ===")
//    println(part1(testInput))
//    println(part2(testInput)) // 11387

    val input = readInput("Day07")
    println("=== RESULT ===")

    val timed1 = measureTimedValue {
        solve(input) { a, b -> listOf(a + b, a * b) }
    }
    println("${timed1.value} (${timed1.duration.inWholeMilliseconds} ms)") // 1620690235709 (127 ms)

    val timed2 = measureTimedValue {
        solve(input) { a, b -> listOf(a + b, a * b, a `||` b) }
    }
    println("${timed2.value} (${timed2.duration.inWholeMilliseconds} ms)") // 145397611075341 (~1.3 sec)

}