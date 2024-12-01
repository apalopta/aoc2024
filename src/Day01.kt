import kotlin.math.abs

fun main() {

    fun convertToLists(list: List<String>): Pair<List<Int>, List<Int>> {
        val l1 = mutableListOf<Int>()
        val l2 = mutableListOf<Int>()

        list.map { li -> li.split("   ").map { it.toInt() } }
            .forEach { (a, b) ->
                l1.add(a)
                l2.add(b)
            }

        return Pair(l1.sorted(), l2.sorted())
    }

    fun part1(a: List<Int>, b: List<Int>): Long =
        a.zip(b).sumOf { (anum, bnum) -> abs(anum - bnum).toLong() }

    fun part2(a: List<Int>, b: List<Int>): Int =
        a.sumOf { anum -> b.count { bnum -> bnum == anum } * anum }

    // Test if implementation meets criteria from the description, like:
//    val (list1, list2) = splitToLists(readInput("test_input"))
//    println(part2(list1, list2))

    // Read the input from the `src/Day01.txt` file.
    val (a, b) = convertToLists(readInput("Day01"))
    part1(a, b).println()
    part2(a, b).println()
}