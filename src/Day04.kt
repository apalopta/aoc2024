import kotlin.math.abs

fun main() {
    data class Location(val x: Int, val y: Int) {
        fun isNeighbourTo(location: Location): Boolean = this != location
                && abs(x - location.x) in listOf(0, 1)
                && abs(y - location.y) in listOf(0, 1)

        override fun toString(): String = "($x, $y)"
    }

    fun Location.calcNextLocation(second: Location): Location {
        val dX = second.x - x
        val dY = second.y - y
        val sX = second.x + if (dX == 0) 0 else if (dX > 0) 1 else -1
        val sY = second.y + if (dY == 0) 0 else if (dY > 0) 1 else -1
        return Location(sX, sY)
    }

    data class FourChars(val x: Location, val m: Location, val a: Location, val s: Location)

    data class ThreeChars(val first: Location, val second: Location, val third: Location) {
        fun expectFourCharsAt(): FourChars {
            val next = second.calcNextLocation(third)
            return FourChars(first, second, third, next)
        }
    }

    data class TwoChars(val first: Location, val second: Location) {
        fun expectThirdAt(): ThreeChars {
            val next = first.calcNextLocation(second)
            return ThreeChars(first, second, next)
        }
    }

    fun findAll(char: Char, input: Array<Array<Char>>): List<Location> {
        val locations = mutableListOf<Location>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == char) {
                    locations.add(Location(i, j))
                }
            }
        }
        return locations.toList()
    }

    fun mas(lines: Array<Array<Char>>): List<ThreeChars> {
        val mLocations = findAll('M', lines)
        val aLocations = findAll('A', lines)
        val mas = aLocations
            .flatMap { a -> mLocations.filter { m -> m.isNeighbourTo(a) }.map { TwoChars(it, a) } }
            .map { it.expectThirdAt() }
            .filterNot { mas -> mas.third.x < 0 || mas.third.y < 0 || mas.third.x >= lines[0].size || mas.third.y >= lines.size }
            .filter { mas -> lines[mas.third.x][mas.third.y] == 'S' }
        return mas
    }

    fun xmas(lines: Array<Array<Char>>): List<FourChars> {
        val xLocations = findAll('X', lines)
        val mLocations = findAll('M', lines)
        val xmsCandidates = mLocations
            .flatMap { m -> xLocations.filter { x -> x.isNeighbourTo(m) }.map { TwoChars(it, m) } }
            .map { it.expectThirdAt() }
            .asSequence()
            .filterNot { xma -> xma.third.x < 0 || xma.third.y < 0 || xma.third.x >= lines[0].size || xma.third.y >= lines.size }
            .filter { xma -> lines[xma.third.x][xma.third.y] == 'A' }
            .map { it.expectFourCharsAt() }
            .filterNot { xmas -> xmas.s.x < 0 || xmas.s.y < 0 || xmas.s.x >= lines[0].size || xmas.s.y >= lines.size }
            .filter { xmas -> lines[xmas.s.x][xmas.s.y] == 'S' }
            .toList()
        return xmsCandidates
    }

    fun part1(lines: Array<Array<Char>>): Int {
        return xmas(lines).size
    }

    fun part2(lines: Array<Array<Char>>): Int {
        val allMas = mas(lines)
        return allMas.flatMap { mas ->
            allMas.filterNot { it == mas }
                .filter { it.second == mas.second }
                .filter {
                    ((it.first.x == mas.first.x && (abs(it.third.y - mas.third.y) == 2))
                            || (it.first.y == mas.first.y && (abs(it.third.x - mas.third.x) == 2)))
                }
        }.size / 2
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