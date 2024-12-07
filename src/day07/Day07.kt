package day07

import utils.*
import kotlin.time.measureTimedValue

infix fun Long.join(other: Long): Long = "$this$other".toLong()

fun main() {
    data class Equation(val res: Long, val numbers: List<Long>)

    fun String.toEquation() = Equation(
        substringBefore(':').toLong(),
        substringAfter(": ").split(' ').map { it.trim().toLong() }
    )

    fun part1(lines: List<String>): Long {
        val equations = lines.map { it.toEquation() }
        val correctEquations = mutableListOf<Long>()

        equations.forEach { equation ->
            var possibleResults = listOf(equation.numbers.first())
            for ((i, num) in equation.numbers.withIndex()) {
                if (i > 0) {
                    possibleResults = possibleResults.flatMap { listOf(it + num, it * num) }
                }
            }
            if (possibleResults.contains(equation.res)) {
                correctEquations.add(equation.res)
            }
        }

        return correctEquations.sum()
    }

    fun part2(lines: List<String>): Long {
        val equations = lines.map { it.toEquation() }
        val correctEquations = mutableListOf<Long>()

        equations.forEach { equation ->
            var possibleResults = listOf(equation.numbers.first())
            for ((i, num) in equation.numbers.withIndex()) {
                if (i > 0) {
                    possibleResults = possibleResults.flatMap {
                        listOf(
                            it + num,
                            it * num,
                            it join num
                        )
                    }
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
    val timed1 = measureTimedValue { part1(input) }
    println("${timed1.value} (${timed1.duration.inWholeMilliseconds} ms)") // 1620690235709 (127 ms)
    val timed2 = measureTimedValue { part2(input) }
    println("${timed2.value} (${timed2.duration.inWholeMilliseconds} ms)") // 145397611075341 (~1.3 sec)

}