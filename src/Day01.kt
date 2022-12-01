fun main() {
    fun List<String>.sumByElves() = joinToString(separator = "/") { it.ifBlank { "|" } }
            .split("/|/")
            .map { elf -> elf.split("/").sumOf { it.toInt() } }
            .sorted()

    fun part1(input: List<String>) = input
            .sumByElves()
            .last()

    fun part2(input: List<String>) = input
            .sumByElves()
            .takeLast(3)
            .sum()

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
