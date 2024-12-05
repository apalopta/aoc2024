package day05

import readInput

data class Rule(val first: Int, val second: Int)
typealias Rules = List<Rule>
typealias Update = List<Int>

fun main() {

    fun Rules.matchingFor(update: Update): Rules = filter { it.first in update && it.second in update }

    fun validOrderFromRules(rules: Rules): List<Int> {
        val sequence = mutableListOf<Int>()
        val myRules = rules.toMutableList()

        // the current very first page cannot appear anywhere as 'second'
        // remove all rules that contain the current very first page as first entry
        // then the remaining rules must contain a new very first page
        // the last remaining rule contains the last two pages
        while (myRules.isNotEmpty()) {
            if (myRules.size == 1) {
                sequence.add(myRules[0].first)
                sequence.add(myRules[0].second)
                myRules.removeAt(0)
            } else {
                // the next very first page is the one 'first' of the remaining rules that is not found in any rule's second entry
                val veryFirst = myRules.first { rule -> rule.first !in myRules.map { it.second } }.first
                sequence.add(veryFirst)
                // rules with this page as first entry are obsolete now
                myRules.removeAll { rule -> rule.first == veryFirst }
            }
        }

        return sequence.toList()
    }

    fun List<String>.getRules() =
        filter { it.contains("|") }
            .map { Rule(it.substringBefore("|").toInt(), it.substringAfter("|").toInt()) }

    fun List<String>.getUpdates() =
        filter { it.contains(",") }
            .map { line -> line.split(",").map { it.toInt() } }

    fun solve(lines: List<String>): Pair<Int, Int> {
        val rules = lines.getRules()
        val updates = lines.getUpdates()

        val validUpdates = mutableListOf<Update>()
        val invalidCorrectedUpdates = mutableListOf<Update>()

        updates.forEach { update ->
            val validOrder = validOrderFromRules(rules.matchingFor(update))
            if (update == validOrder) {
                validUpdates.add(update)
            } else {
                invalidCorrectedUpdates.add(validOrder)
            }
        }

        return Pair(
            validUpdates.sumOf { it[(it.size / 2)] },
            invalidCorrectedUpdates.sumOf { it[(it.size / 2)] }
        )
    }

    // Test if implementation meets criteria from the description, like:
//    val testInput = readTestInput("day05/test_input")
//    val testResult = solve(testInput)
//    println("=== TEST ====")
//    println(testResult.first)
//    println(testResult.second)

    val input = readInput("Day05")

    val (valid, invalidCorrected) = solve(input)
    println("=== RES ====")
    println(valid)
    println(invalidCorrected)
}