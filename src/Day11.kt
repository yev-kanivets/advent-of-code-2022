fun main() {

    data class Monkey(
        var items: List<Long>,
        var inspectionCount: Long,
        val operation: Char,
        val argument: Long?,
        val test: Long,
        val trueThrow: Int,
        val falseThrow: Int,
    )

    fun List<String>.parse() = chunked(7).map { input ->
        Monkey(
            items = input[1].split(":").last().split(",").map { it.trim().toLong() },
            inspectionCount = 0,
            operation = if (input[2].contains("+")) '+' else '*',
            argument = input[2].split(" ").last().toLongOrNull(),
            test = input[3].split(" ").last().toLong(),
            trueThrow = input[4].split(" ").last().toInt(),
            falseThrow = input[5].split(" ").last().toInt(),
        )
    }

    fun List<Monkey>.simulate(rounds: Int, relief: Int) = let { monkeys ->
        val lcd = monkeys.map { it.test }.reduce { acc, i -> acc * i } // Not really LCD, but works too
        repeat(rounds) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    monkey.inspectionCount++
                    val newItem = when (monkey.operation) {
                        '+' -> item + (monkey.argument ?: item)
                        else -> item * (monkey.argument ?: item)
                    } / relief
                    if (newItem % monkey.test == 0L) {
                        monkeys[monkey.trueThrow].items += (newItem % lcd)
                    } else {
                        monkeys[monkey.falseThrow].items += (newItem % lcd)
                    }
                }
                monkey.items = emptyList()
            }
        }
        monkeys
    }

    fun List<Monkey>.business() = map { it.inspectionCount }
        .sorted()
        .takeLast(2)
        .reduce { acc, i -> acc * i }

    fun part1(input: List<String>) = input.parse()
        .simulate(rounds = 20, relief = 3)
        .business()

    fun part2(input: List<String>) = input.parse()
        .simulate(rounds = 10000, relief = 1)
        .business()

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
