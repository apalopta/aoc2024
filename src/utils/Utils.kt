package utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

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

data class Position(val x: Int, val y: Int) {
    override fun toString(): String = "($x, $y)"
    fun distanceTo(other: Position): Position = Position(other.x - x, other.y - y)
    fun fromHere(other: Position): Position = Position(x + other.x, y + other.y)
    fun fromHereBackwards(other: Position): Position = Position(x - other.x, y - other.y)
}

fun Position.move(direction: Direction) = when (direction) {
    Direction.NORTH -> Position(x - 1, y)
    Direction.EAST -> Position(x, y + 1)
    Direction.SOUTH -> Position(x + 1, y)
    Direction.WEST -> Position(x, y - 1)
}

typealias Area = Array<Array<Char>>

fun Area.containsLocation(position: Position) =
    (position.x >= 0 && position.y >= 0 && position.x < this[0].size && position.y < size)

fun Area.containsCharAtLocation(char: Char, position: Position) =
    this[position.x][position.y] == char

fun Area.nextLocation(position: Position, direction: Direction): Position? {
    val next = position.move(direction)
    return if (this.containsLocation(next)) next else null
}

fun Area.findAll(char: Char): List<Position> {
    val positions = mutableListOf<Position>()
    for (i in indices) {
        for (j in this[i].indices) {
            if (this[i][j] == char) {
                positions.add(Position(i, j))
            }
        }
    }
    return positions.toList()
}
fun Area.findAllNot(char: Char): List<Position> {
    val positions = mutableListOf<Position>()
    for (i in indices) {
        for (j in this[i].indices) {
            if (this[i][j] != char) {
                positions.add(Position(i, j))
            }
        }
    }
    return positions.toList()
}

val Area.allPositions: List<Position>
    get() {
        val positions = mutableListOf<Position>()
        for (i in indices) {
            for (j in this[i].indices) {
                positions.add(Position(i, j))
            }
        }
        return positions.toList()
    }

fun Area.positionsFromHereTowardsDirection(here: Position, direction: Direction): List<Position> =
    when (direction) {
        Direction.NORTH -> allPositions.filter { it.x < here.x && it.y == here.y }.sortedByDescending { it.x }
        Direction.EAST -> allPositions.filter { it.x == here.x && it.y > here.y }.sortedBy { it.y }
        Direction.SOUTH -> allPositions.filter { it.x > here.x && it.y == here.y }.sortedBy { it.x }
        Direction.WEST -> allPositions.filter { it.x == here.x && it.y < here.y }.sortedByDescending { it.y }
    }