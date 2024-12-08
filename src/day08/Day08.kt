package day08

import utils.*
import kotlin.time.measureTimedValue

fun main() {

    fun part1(area: Area, antennaLocations: Map<Char, List<Position>>): Int {
        val antinodes = mutableSetOf<Position>()

        antennaLocations.forEach { (_, positions) ->
            positions.forEach { pos ->
                positions.exceptForPos(pos)
                    .forEach { other ->
                        val dist = pos.distanceTo(other)
                        val antinode = pos.fromHereBackwards(dist)
                        if (area.containsLocation(antinode))
                            antinodes.add(antinode)
                    }
                }
        }

        return antinodes.size
    }

    fun part2(area: Area, antennaLocations: Map<Char, List<Position>>): Int {
        val antinodes = mutableSetOf<Position>()

        antennaLocations.forEach { (_, positions) ->
            positions.forEach { pos ->
                positions.exceptForPos(pos)
                    .forEach { other ->
                        antinodes.add(pos)
                        val dist = pos.distanceTo(other)
                        var antinode = pos.fromHereBackwards(dist)
                        while (area.containsLocation(antinode)) {
                            antinodes.add(antinode)
                            antinode = antinode.fromHereBackwards(dist)
                        }
                    }
            }
        }

        return antinodes.size
    }

    val input = readInput("Day08")
    val area = input.toArea()
    val antennaLocations = input.joinToString().toCharArray().distinct().toMutableList()
        .filter { it.isDigit() || it.isLetter() }
        .associateWith { area.findAll(it) }

    val res1 = measureTimedValue { part1(area, antennaLocations) }
    val res2 = measureTimedValue { part2(area, antennaLocations) }

//    part1: 276 (2 ms)
//    part2: 991 (1 ms)
    println("part1: ${res1.value} (${res1.duration.inWholeMilliseconds} ms)")
    println("part2: ${res2.value} (${res2.duration.inWholeMilliseconds} ms)")
}