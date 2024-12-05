package day05

import println
import readInput
import readTestInput

fun main() {
    data class Ordering(val first: Int, val second: Int)
    data class Rules(val orderings: List<Ordering>)
    data class Update(val pages: List<Int>)

    fun Update.matchingOrderings(rules: Rules): List<Ordering> =
        rules.orderings.filter { it.first in this.pages && it.second in this.pages }

    fun sequenceFromOrdering(orderings: List<Ordering>): List<Int> {
        val sequence = mutableListOf<Int>()
        val myOrderings = orderings.toMutableList()

        while (myOrderings.isNotEmpty()) {
            if (myOrderings.size == 1) {
                sequence.add(myOrderings[0].first)
                sequence.add(myOrderings[0].second)
                myOrderings.removeAt(0)
            } else {
                val veryFirst = myOrderings.first { ordering -> ordering.first !in myOrderings.map { it.second } }.first
                sequence.add(veryFirst)
                myOrderings.removeAll { ordering -> ordering.first == veryFirst }
            }
        }

        return sequence.toList()
    }

    fun part1(lines: List<String>): Int {
        val rules = Rules(lines.filter { it.contains("|") }
            .map { Ordering(it.substringBefore("|").toInt(), it.substringAfter("|").toInt()) })

        val updates = lines.filter { it.contains(",") }.map { line ->
            line.split(",").map { it.toInt() }
        }.map { Update(it) }

        val validUpdates = mutableListOf<Update>()
        updates.forEach { update ->
            val matchingOrderings = update.matchingOrderings(rules).toMutableList()
            val validSequence = sequenceFromOrdering(matchingOrderings)
            if (update.pages == validSequence) {
                validUpdates.add(update)
            }
        }

        return validUpdates.sumOf {
            val pages = it.pages
            pages[(pages.size / 2)]
        }
    }

    fun part2(lines: List<String>): Int {
        val rules = Rules(lines.filter { it.contains("|") }
            .map { Ordering(it.substringBefore("|").toInt(), it.substringAfter("|").toInt()) })

        val updates = lines.filter { it.contains(",") }.map { line ->
            line.split(",").map { it.toInt() }
        }.map { Update(it) }

        val invalidCorrectedUpdates = mutableListOf<Update>()
        updates.forEach { update ->
            val matchingOrderings = update.matchingOrderings(rules).toMutableList()
            val validSequence = sequenceFromOrdering(matchingOrderings)
            if (update.pages != validSequence) {
                invalidCorrectedUpdates.add(Update(validSequence))
            }
        }

        return invalidCorrectedUpdates.sumOf {
            val pages = it.pages
            pages[(pages.size / 2)]
        }
    }

    // Test if implementation meets criteria from the description, like:
    val testInput = readTestInput("day05/test_input")
    println("=== TEST ====")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day05")

    println("=== RES ====")
    println(part1(input))
    println(part2(input))

}