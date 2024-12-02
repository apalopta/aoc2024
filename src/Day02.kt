import kotlin.math.abs

fun main() {
    fun List<String>.toIntLists() = map { line -> line.split(" ").map { it.toInt() } }

    fun dampenedLists(line: List<Int>): List<List<Int>> {
        val dampened = mutableListOf<List<Int>>(line)
        for (i in line.indices) {
            val newList = mutableListOf<Int>()
            line.forEachIndexed { index, num ->
                if (index != i) newList.add(num)
            }
            dampened.add(newList)
        }
        return dampened.toList()
    }

    fun isSafe(line: List<Int>) = ((line == line.sorted() || line == line.sortedDescending())
            && (line.zipWithNext { a, b -> (abs(a - b) < 4) && (abs(a - b) > 0) }.all { it })
            && (line.distinct().size == line.size))

    fun part1(lines: List<List<Int>>): Int = lines.map { line ->
        isSafe(line)
    }.count { it }

    fun part2(lines: List<List<Int>>): Int = lines.map { line ->
        isSafe(line) || dampenedLists(line).any { isSafe(it) }
    }.count { it }

    // Test if implementation meets criteria from the description, like:
//    val lines = readInput("test_input").toIntLists()
//    println(part1(lines))
//    println(part2(lines))

    val input = readInput("Day02").toIntLists()

    println(part1(input))
    println(part2(input))

}