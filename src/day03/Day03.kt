package day03

import utils.readInput

fun main() {
    val regex = """mul\((?<a>\d{1,3}),(?<b>\d{1,3})\)""".toRegex()

    fun sumOfMultsInString(string: String) =
        regex.findAll(string).map {
            val a = it.groups["a"]?.value!!.toInt()
            val b = it.groups["b"]?.value!!.toInt()
            a * b
        }.sum()

    fun allMults(lines: List<String>): Int {
        return lines.sumOf { line ->
            sumOfMultsInString(line)
        }
    }

    fun String.nextIndexOfDont(iOfDo: Int) =
        indexOf("don't()", iOfDo).let { if (it == -1) length - 1 else it }

    fun String.nextIndexOfDo(iOfDont: Int) =
        indexOf("do()", iOfDont).let { if (it == -1) length - 1 else it }

    fun allMultsWithoutDonts(lines: List<String>): Int {
        val line = lines.joinToString("")
        val values = mutableListOf<Int>()
        var iOfDo = 0
        var iOfDont = line.nextIndexOfDont(iOfDo)
        values.add(sumOfMultsInString(line.substring(iOfDo, iOfDont)))
        iOfDo = line.nextIndexOfDo(iOfDont)
        while (iOfDo < line.length - 1) {
            iOfDont = line.nextIndexOfDont(iOfDo)
            values.add(sumOfMultsInString(line.substring(iOfDo, iOfDont)))
            iOfDo = line.nextIndexOfDo(iOfDont)
        }
        return values.sum()
    }

    fun part1(lines: List<String>): Int {
        return allMults(lines)
    }

    fun part2(lines: List<String>): Int {
        return allMultsWithoutDonts(lines)
    }

    // Test if implementation meets criteria from the description, like:
//    val testInput = readInput("test_input")
//    println(part1(testInput))
//    println(part2(testInput))

    val input = readInput("Day03")

    println(part1(input))
    println(part2(input))

}