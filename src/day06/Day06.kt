package day06

import utils.*

const val obstacle = '#'

data class PosWithDir(val pos: Position, val dir: Direction)

class Guard(startPos: Position, startDirection: Direction, private val map: Area) {
    private var direction = startDirection
    private var pos = startPos
    var isPresent = true
    val visitedPositions = mutableListOf(PosWithDir(pos, direction))
    val additionalObstacles = mutableListOf<Position>()

    fun move() {
        maybePlaceObstacle()

        var nextPos = map.nextLocation(pos, direction)
        if (nextPos != null) {
            if (!hitsObstacle(nextPos)) {
                pos = nextPos
                visitedPositions.add(PosWithDir(pos, direction))
            } else {
                val newDirection = direction.turnRight()
                nextPos = map.nextLocation(pos, newDirection)
                if (nextPos != null) {
                    direction = newDirection
                    pos = nextPos
                    visitedPositions.add(PosWithDir(pos, direction))
                } else {
                    isPresent = false
                }
            }
        } else {
            isPresent = false
        }
    }

    private fun maybePlaceObstacle() {
        val positionsWithDirToRight = map.positionsFromHereTowardsDirection(pos, direction.turnRight())
            .map { PosWithDir(it, direction.turnRight()) }
        if (positionsWithDirToRight.any { it in visitedPositions }) {
            map.nextLocation(pos, direction)?.let {
                additionalObstacles.add(it)
            }
        }
    }

    private fun hitsObstacle(nextPos: Position) =
        map.containsCharAtLocation(obstacle, nextPos)
}

fun main() {

    fun solve(area: Area): Pair<Int, Int> {
        val startPos = area.findAll('^').first()
        val guard = Guard(startPos, Direction.NORTH, area)

        while (guard.isPresent) {
            guard.move()
        }

        return Pair(
            guard.visitedPositions.map { it.pos }.distinct().size,
            guard.additionalObstacles.filterNot { it == startPos }.distinct().size
        )

    }

    // Test if implementation meets criteria from the description, like:
    val testInput = readTestInput("day06/test_input")
        .map { it.toCharArray().toTypedArray() }
        .toTypedArray()
    val tesResult = solve(testInput)
    println("=== TEST ===")
    println(tesResult.first)    // 41
    println(tesResult.second)   // 6

    val input = readInput("Day06")
        .map { it.toCharArray().toTypedArray() }
        .toTypedArray()
    println("=== RESULT ===")
    val res = solve(input)
    println(res.first)          // 4982
    println(res.second)         // 636 ?!?

}