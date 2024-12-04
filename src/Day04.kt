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

    data class FourChars(val first: Location, val second: Location, val third: Location, val fourth: Location)

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

    fun Array<Array<Char>>.findAll(char: Char): List<Location> {
        val locations = mutableListOf<Location>()
        for (i in indices) {
            for (j in this[i].indices) {
                if (this[i][j] == char) {
                    locations.add(Location(i, j))
                }
            }
        }
        return locations.toList()
    }

    fun Array<Array<Char>>.locationMatchesChar(loc: Location, char: Char) = this[loc.x][loc.y] == char

    fun Array<Array<Char>>.containsLocation(location: Location) =
        location.x >= 0 && location.y >= 0 && location.x < this[0].size && location.y < size

    fun mas(lines: Array<Array<Char>>): List<ThreeChars> {
        val mLocations = lines.findAll('M')
        val aLocations = lines.findAll('A')
        return aLocations
            .flatMap { a -> mLocations.filter { m -> m.isNeighbourTo(a) }.map { TwoChars(it, a) } }
            .map { it.expectThirdAt() }
            .filter { mas -> lines.containsLocation(mas.third) }
            .filter { mas -> lines.locationMatchesChar(mas.third, 'S') }
    }

    fun xmas(lines: Array<Array<Char>>): List<FourChars> {
        val xLocations = lines.findAll('X')
        val mLocations = lines.findAll('M')
        return mLocations
            .flatMap { m -> xLocations.filter { x -> x.isNeighbourTo(m) }.map { TwoChars(it, m) } }
            .map { it.expectThirdAt() }
            .asSequence()
            .filterNot { xma -> lines.containsLocation(xma.third) }
            .filter { xma -> lines.locationMatchesChar(xma.third, 'A') }
            .map { it.expectFourCharsAt() }
            .filterNot { xmas -> xmas.fourth.x < 0 || xmas.fourth.y < 0 || xmas.fourth.x >= lines[0].size || xmas.fourth.y >= lines.size }
            .filter { xmas -> lines.locationMatchesChar(xmas.fourth, 'S') }
            .toList()
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