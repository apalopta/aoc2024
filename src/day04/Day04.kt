package day04

import Location
import findAll
import readInput
import kotlin.math.abs

fun main() {

    fun Location.isNeighbourTo(location: Location): Boolean = this != location
            && abs(x - location.x) in listOf(0, 1)
            && abs(y - location.y) in listOf(0, 1)

    fun Location.calcNextLocation(second: Location): Location {
        val dX = second.x - x
        val dY = second.y - y
        val sX = second.x + if (dX == 0) 0 else if (dX > 0) 1 else -1
        val sY = second.y + if (dY == 0) 0 else if (dY > 0) 1 else -1
        return Location(sX, sY)
    }

    data class FourChars(val first: Location, val second: Location, val third: Location, val fourth: Location)

    data class ThreeChars(val first: Location, val second: Location, val third: Location) {
        fun expectFourCharsAt(): FourChars = FourChars(first, second, third, second.calcNextLocation(third))
    }

    data class TwoChars(val first: Location, val second: Location) {
        fun expectThreeCharsAt(): ThreeChars = ThreeChars(first, second, first.calcNextLocation(second))
    }

    fun Array<Array<Char>>.containsCharAtLocation(char: Char, location: Location) =
        (location.x >= 0 && location.y >= 0 && location.x < this[0].size && location.y < size)
                && this[location.x][location.y] == char

    fun Array<Array<Char>>.allMas(): List<ThreeChars> {
        val mLocations = findAll('M')
        val aLocations = findAll('A')
        return aLocations
            .flatMap { a -> mLocations.filter { m -> m.isNeighbourTo(a) }.map { TwoChars(it, a) } }
            .map { it.expectThreeCharsAt() }
            .filter { mas -> containsCharAtLocation('S', mas.third) }
    }

    fun Array<Array<Char>>.allXmas(): List<FourChars> {
        val xLocations = findAll('X')
        val mLocations = findAll('M')
        return mLocations
            .flatMap { m -> xLocations.filter { x -> x.isNeighbourTo(m) }.map { TwoChars(it, m) } }
            .map { it.expectThreeCharsAt() }
            .asSequence()
            .filter { xma -> containsCharAtLocation('A', xma.third) }
            .map { it.expectFourCharsAt() }
            .filter { xmas -> containsCharAtLocation('S', xmas.fourth) }
            .toList()
    }

    fun part1(grid: Array<Array<Char>>): Int {
        return grid.allXmas().size
    }

    // m s    s m    m m    s s
    //  α  or  a  or  a  or  a
    // m s    s m    s s    m m
    fun formsCross(a: ThreeChars, b: ThreeChars) =
        ((a.first.x == b.first.x && (abs(a.third.y - b.third.y) == 2))
                || (a.first.y == b.first.y && (abs(a.third.x - b.third.x) == 2)))

    fun `allX-MAS`(allMas: List<ThreeChars>): List<ThreeChars> {
        return allMas
            .flatMap { mas ->
                allMas.filter {
                    it.second == mas.second && it != mas && formsCross(it, mas)
                }
            }.also { check(it.size % 2 == 0) { "expected even number of x-mas pairs" } }
    }

    fun part2(grid: Array<Array<Char>>): Int {
        val allMas = grid.allMas()
        return `allX-MAS`(allMas).size / 2
    }

    // Test if implementation meets criteria from the description, like:
//    val testInput = readInput("test_input")
//        .map { it.toCharArray().toTypedArray() }
//        .toTypedArray()
//    println(part1(testInput))
//    println(part2(testInput))

    val input = readInput("Day04")
        .map { it.toCharArray().toTypedArray() }
        .toTypedArray()
    println(part1(input))
    println(part2(input))

}