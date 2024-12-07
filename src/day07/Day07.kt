package day07

import utils.*

fun main() {
    data class Equation(val res: Long, val numbers: List<Int>)

    fun String.toEquation() = Equation(
        substringBefore(':').toLong(),
        substringAfter(": ").split(' ').map { it.trim().toInt() }
    )

    fun part1(lines: List<String>): Long {
        val equations = lines.map { it.toEquation() }

        val correctEquations = mutableListOf<Long>()

        equations.forEach { equation ->
            var possibleResults = listOf<Long>(equation.numbers.first().toLong())
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
            var possibleResults = listOf(equation.numbers.first().toLong())
            for ((i, num) in equation.numbers.withIndex()) {
                if (i > 0) {
                    possibleResults = possibleResults.flatMap {
                        listOf(
                            it + num,
                            it * num,
                            "$it$num".toLong()
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
    val testInput = readTestInput("day07/test_input")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input)) // 145 397 522 322 159

}