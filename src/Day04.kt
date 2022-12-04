import kotlin.math.min

fun main() {

    fun List<String>.parse() = map { pair ->
        pair.split(',').map { assignment ->
            assignment.split('-').map { it.toInt() }
        }.map { it.first()..it.last() }
    }

    fun part1(input: List<String>) = input
            .parse()
            .count { it.first().intersect(it.last()).size == min(it.first().count(), it.last().count()) }

    fun part2(input: List<String>) = input
            .parse()
            .count { it.first().intersect(it.last()).isNotEmpty() }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
