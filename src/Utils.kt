import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/${name.lowercase()}/$name.txt").readText().trim().lines()

fun readInputAsString(name: String) = Path("src/${name.lowercase()}/$name.txt").readText().trim()

fun readTestInput(path: String) = Path("src/$path.txt").readText().trim().lines()

fun readTestInputAsString(path: String) = Path("src/$path.txt").readText().trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

enum class Direction(symbol: Char) {
    NORTH('^'),
    EAST('>'),
    SOUTH('v'),
    WEST('<');
}

fun Direction.turnRight() = when (this) {
    Direction.NORTH -> Direction.EAST
    Direction.EAST -> Direction.SOUTH
    Direction.SOUTH -> Direction.WEST
    Direction.WEST -> Direction.NORTH
}
fun Direction.opposite() = when (this) {
    Direction.NORTH -> Direction.SOUTH
    Direction.EAST -> Direction.WEST
    Direction.SOUTH -> Direction.NORTH
    Direction.WEST -> Direction.EAST
}

data class Location(val x: Int, val y: Int) {


    override fun toString(): String = "($x, $y)"
}

fun Location.move(direction: Direction) = when (direction) {
    Direction.NORTH -> Location(x - 1, y)
    Direction.EAST -> Location(x, y + 1)
    Direction.SOUTH -> Location(x + 1, y)
    Direction.WEST -> Location(x, y - 1)
}

typealias Area = Array<Array<Char>>

fun Area.containsLocation(location: Location) =
    (location.x >= 0 && location.y >= 0 && location.x < this[0].size && location.y < size)

fun Area.containsCharAtLocation(char: Char, location: Location) =
    this[location.x][location.y] == char

fun Area.nextLocation(location: Location, direction: Direction): Location? {
    val next = location.move(direction)
    return if (this.containsLocation(next)) next else null
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
