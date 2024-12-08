package day08

import utils.*

fun main() {
    fun part1(area: Area, antennaFrequencies: List<Char>): Int {

        val antennaLocations = antennaFrequencies.associateWith { area.findAll(it) }

        val antinodes = mutableSetOf<Position>()
        antennaLocations.forEach { (aType, positions) ->
            println("$aType -> ${positions.joinToString(", ")}")
            positions.forEach { pos ->
                positions.filter { it != pos }.forEach { other ->
                    val dist = pos.distanceTo(other)
                    val antinode = pos.fromHereBackwards(dist)
                    if (area.containsLocation(antinode)) antinodes.add(antinode)
                }
            }
        }

        return antinodes.size
    }

    fun part2(lines: List<String>): Int = 1

    // Test if implementation meets criteria from the description, like:
//    val testInput = utils.readTestInput("day08/test_input")
//    val antennaFrequencies = testInput.joinToString().toCharArray().distinct().toMutableList()
//        .filter { it.isDigit() || it.isLetter() }
//    val area = testInput
//        .map { it.toCharArray().toTypedArray() }
//        .toTypedArray()
//    println(part1(area, antennaFrequencies))
//    println(part2(testInput))

    val input = readInput("Day08")
    val antennaFrequencies = input.joinToString().toCharArray().distinct().toMutableList()
        .filter { it.isDigit() || it.isLetter() }
    val area = input
        .map { it.toCharArray().toTypedArray() }
        .toTypedArray()
    println(part1(area, antennaFrequencies))
//    println(part2(input))

}