import java.lang.IllegalArgumentException
import kotlin.math.pow

fun main() {

    fun List<String>.parse() = map { snafu ->
        snafu.reversed().mapIndexed { index, digit ->
            when (digit) {
                '=' -> -2
                '-' -> -1
                '0' -> 0
                '1' -> 1
                '2' -> 2
                else -> throw IllegalArgumentException()
            } * 5.0.pow(index)
        }.sumOf { it.toLong() }
    }

    fun Long.toSnafu() = let { normal ->
        var power = 1L
        var remainder = normal
        var snafu = ""

        while (remainder != 0L) {
            val snafuDigit = when (remainder % (5L * power)) {
                0L -> '0'
                1L * power -> '1'.also { remainder -= 1 * power }
                2L * power -> '2'.also { remainder -= 2 * power }
                3L * power -> '='.also { remainder += 2 * power }
                4L * power -> '-'.also { remainder += 1 * power }
                else -> throw IllegalArgumentException()
            }
            power *= 5
            snafu += snafuDigit
        }

        snafu.reversed()
    }

    fun part1(input: List<String>) = input.parse().sum().toSnafu()

    fun part2(input: List<String>) = input.parse().let { "Merry Christmas & Happy New Year!" }

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}

// 10-10-==22=1--