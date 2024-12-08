package day08

import utils.*

fun main() {
    fun part1(area: Area, antennaLocations: Map<Char, List<Position>>): Int {

        val antinodes = mutableSetOf<Position>()
        antennaLocations.forEach { (aType, positions) ->
            positions.forEach { pos ->
                positions.filter { it != pos }
                    .forEach { other ->
                        val dist = pos.distanceTo(other)
                        val antinode = pos.fromHereBackwards(dist)
                        if (area.containsLocation(antinode)) antinodes.add(antinode)
                    }
            }
        }

        return antinodes.size
    }

    fun part2(area: Area, antennaLocations: Map<Char, List<Position>>): Int {
        val antinodes = mutableSetOf<Position>()
        antennaLocations.forEach { (aType, positions) ->
            positions.forEach { pos ->
                positions.filter { it != pos }
                    .forEach { other ->
                        val dist = pos.distanceTo(other)
                        var antinode = pos.fromHereBackwards(dist)
                        antinodes.add(pos)
                        if (area.containsLocation(antinode)) {
                            antinodes.add(antinode)
                        }
                        while (area.containsLocation(antinode)) {
                            antinode = antinode.fromHereBackwards(dist)
                            if (area.containsLocation(antinode)) antinodes.add(antinode)
                        }
                    }
            }
        }

        return antinodes.distinct().size
    }

    // Test if implementation meets criteria from the description, like:
//    val testInput = utils.readTestInput("day08/test_input")
//    val antennaFrequencies = testInput.joinToString().toCharArray().distinct().toMutableList()
//        .filter { it.isDigit() || it.isLetter() }
//    val area = testInput
//        .map { it.toCharArray().toTypedArray() }
//        .toTypedArray()
//    val antennaLocations = antennaFrequencies.associateWith { area.findAll(it) }
//    println(part1(area, antennaLocations))
//    println(part2(area, antennaLocations))

    val input = readInput("Day08")
    val antennaFrequencies = input.joinToString().toCharArray().distinct().toMutableList()
        .filter { it.isDigit() || it.isLetter() }
    val area = input
        .map { it.toCharArray().toTypedArray() }
        .toTypedArray()
    val antennaLocations = antennaFrequencies.associateWith { area.findAll(it) }
    println(part1(area, antennaLocations))
    println(part2(area, antennaLocations))

}