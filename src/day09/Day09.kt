package day09

import utils.readInput
import kotlin.time.measureTimedValue

fun main() {
    fun String.createDisk(): List<Int?> {
        val disk = buildList {
            var index = 0
            this@createDisk.toCharArray().map { it.digitToInt() }
                .forEachIndexed { i, l ->
                    if (i % 2 == 0) {  // file
                        this@buildList.addAll(List(l) { index })
                        index++
                    } else { // space
                        this@buildList.addAll(List(l) { null })
                    }
                }
        }
        return disk.toList()
    }

    fun part1(input: String): Long {
        val disk = input.createDisk().toTypedArray()
//            .also { println(it.joinToString("")) }
        var lastUsedBlock = disk.indexOfLast { it != null }
        var firstFree = disk.indexOfFirst { it == null }

//        println("lastUsedBlock = $lastUsedBlock")
//        println("firstFree = $firstFree")
//00...111...2...333.44.5555.6666.777.888899

        while (lastUsedBlock > firstFree) {
            val file = disk[lastUsedBlock]
            disk[firstFree] = file
            disk[lastUsedBlock] = null

//            println(disk.toCharArray())
//            println("firstFree $firstFree - lastUsed $lastUsedBlock")

            lastUsedBlock = disk.indexOfLast { it != null }
            firstFree = disk.indexOfFirst { it == null }

        }

//        println(disk.toList())
        val res = disk.toList().filterNotNull()
            .mapIndexed { i, c -> i * c.toLong() }.sum()

        return res
    }

    fun part2(input: String): Int = 1

    // Test if implementation meets criteria from the description, like:
    val testInput = utils.readTestInput("day09/test_input").joinToString("").trim()
    println(part1(testInput)) // 1928
//    println(part2(testInput))

    val input = readInput("Day09").joinToString("").trim()
    val res1 = measureTimedValue { part1(input) } // 6334655979668
    println("res1: ${res1.value} (${res1.duration.inWholeMilliseconds} ms)")
//    println(part2(input))

}
