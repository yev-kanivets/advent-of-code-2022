import kotlin.math.abs

fun main() {

    fun List<String>.simulate() = let { instructions ->
        var x = 1
        return@let listOf(1) + instructions.map { instruction ->
            if (instruction == "noop") {
                listOf(x)
            } else {
                val value = instruction.split(" ").last().toInt()
                x += value
                listOf(x - value, x)
            }
        }.flatten()
    }

    fun part1(input: List<String>) = input.simulate()
        .mapIndexed { index, x -> (index + 1) to x }
        .filter { (cycle, _) -> cycle % 40 == 20 }
        .sumOf { (cycle, x) -> cycle * x }

    fun part2(input: List<String>) = input.simulate()
        .mapIndexed { index, spriteIndex -> if (abs(index % 40 - spriteIndex) <= 1) '#' else '.' }
        .chunked(40)
        .joinToString(separator = "\n") { it.joinToString(separator = "") }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
