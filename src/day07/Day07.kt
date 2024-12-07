package day07

import kotlinx.coroutines.*
import utils.*
import kotlin.time.measureTimedValue

fun main() {

    data class Equation(val res: Long, val numbers: List<Long>)

    infix fun Long.`||`(other: Long): Long = "$this$other".toLong()

    fun String.toEquation() = Equation(
        substringBefore(':').toLong(),
        substringAfter(": ").split(' ').map { it.trim().toLong() }
    )

    fun solve(lines: List<String>, opVariants: (Long, Long) -> List<Long>) = runBlocking {
        lines.map { line ->
            async(Dispatchers.Default) {
                val equation = line.toEquation()
                var possibleResults = listOf(equation.numbers.first())
                for ((i, num) in equation.numbers.withIndex()) {
                    if (i > 0) {
                        possibleResults = possibleResults.flatMap { opVariants(it, num) }
                    }
                }
                if (possibleResults.contains(equation.res)) {
                    equation.res
                } else {
                    null
                }
            }
        }.awaitAll().mapNotNull { it }.sum()
    }

    val input = readInput("Day07")

    listOf(
        { a: Long, b: Long -> listOf(a + b, a * b) },           // 1620690235709 (w/o coroutines: 127 ms, w/ coroutines: ~200 ms)
        { a: Long, b: Long -> listOf(a + b, a * b, a `||` b) }  // 145397611075341 (w/o coroutines: ~1.3 sec, w/ coroutines: ~750 ms)
    ).forEach { opVariants ->
        val (result, duration) = measureTimedValue {
            solve(input, opVariants)
        }
        println("$result (${duration.inWholeMilliseconds} ms)")
    }
}