fun main() {

    fun List<String>.parse() = mapIndexed { y, row ->
        row.mapIndexedNotNull { x, cell -> if (cell == '#') y to x else null }
    }.flatten().toSet()

    fun Pair<Int, Int>.adjNorth() = listOf(
        first - 1 to second,
        first - 1 to second - 1,
        first - 1 to second + 1,
    )

    fun Pair<Int, Int>.adjSouth() = listOf(
        first + 1 to second,
        first + 1 to second - 1,
        first + 1 to second + 1,
    )

    fun Pair<Int, Int>.adjWest() = listOf(
        first to second - 1,
        first - 1 to second - 1,
        first + 1 to second - 1,
    )

    fun Pair<Int, Int>.adjEast() = listOf(
        first to second + 1,
        first - 1 to second + 1,
        first + 1 to second + 1,
    )

    fun Set<Pair<Int, Int>>.considerMove(position: Pair<Int, Int>, iteration: Int): Pair<Int, Int>? {
        val moves = listOf(position.adjNorth(), position.adjSouth(), position.adjWest(), position.adjEast())
        if (moves.flatten().none { cell -> contains(cell) }) return null
        val shift = iteration % moves.size
        val shiftedMoves = moves.subList(shift, moves.size) + moves.subList(0, shift)
        return shiftedMoves.firstOrNull { it.none { cell -> contains(cell) } }?.first()
    }

    fun part1(input: List<String>) = input.parse().let { startElves ->
        var elves = startElves

        repeat(10) { iteration ->
            val consideredMoves = elves.map { elves.considerMove(it, iteration) ?: it }
            elves = elves.mapIndexed { index, elf ->
                val consideredMove = consideredMoves[index]
                if (consideredMoves.count { it == consideredMove } == 1) consideredMove else elf
            }.toSet()
        }

        (elves.maxOf { it.first } - elves.minOf { it.first } + 1) * (elves.maxOf { it.second } - elves.minOf { it.second } + 1) - elves.size
    }

    fun part2(input: List<String>) = input.parse().let { startElves ->
        var elves = startElves
        var iteration = 0

        while (true) {
            val consideredMoves = elves.map { elves.considerMove(it, iteration) ?: it }
            if (consideredMoves.toSet() == elves) break

            elves = elves.mapIndexed { index, elf ->
                val consideredMove = consideredMoves[index]
                if (consideredMoves.count { it == consideredMove } == 1) consideredMove else elf
            }.toSet()

            iteration++
        }

        iteration + 1
    }

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
