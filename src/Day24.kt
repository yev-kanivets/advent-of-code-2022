data class Blizzard(
    val direction: Direction,
    val maxY: Int,
    val maxX: Int,
    val y: Int,
    val x: Int,
) {

    enum class Direction { R, D, L, U }

    fun move() = when (direction) {
        Direction.R -> copy(x = if (x == maxX) 1 else x + 1)
        Direction.D -> copy(y = if (y == maxY) 1 else y + 1)
        Direction.L -> copy(x = if (x == 1) maxX else x - 1)
        Direction.U -> copy(y = if (y == 1) maxY else y - 1)
    }
}

fun main() {

    fun List<String>.parse() = mapIndexed { y, row ->
        row.mapIndexedNotNull { x, cell ->
            when (cell) {
                '>' -> Blizzard(Blizzard.Direction.R, lastIndex - 1, row.lastIndex - 1, y, x)
                'v' -> Blizzard(Blizzard.Direction.D, lastIndex - 1, row.lastIndex - 1, y, x)
                '<' -> Blizzard(Blizzard.Direction.L, lastIndex - 1, row.lastIndex - 1, y, x)
                '^' -> Blizzard(Blizzard.Direction.U, lastIndex - 1, row.lastIndex - 1, y, x)
                else -> null
            }
        }
    }.flatten()

    fun Pair<Int, Int>.moves() = listOf(
        first - 1 to second,
        first to second,
        first to second - 1,
        first to second + 1,
        first + 1 to second,
    )

    fun List<Blizzard>.simulate(start: Pair<Int, Int>, end: Pair<Int, Int>) = let { startBlizzards ->
        val yRange = 1..startBlizzards.first().maxY
        val xRange = 1..startBlizzards.first().maxX
        var blizzards = startBlizzards
        var positions = setOf(start)
        var minutes = 0

        while (positions.none { it == end }) {
            minutes++
            blizzards = blizzards.map { it.move() }

            val blizzardPositions = blizzards.map { it.y to it.x }.toSet()
            positions = positions.asSequence()
                .map { it.moves() }
                .flatten()
                .filter { (it.first in yRange && it.second in xRange) || it == start || it == end }
                .filterNot { it in blizzardPositions }
                .toSet()
        }

        blizzards to minutes
    }

    fun part1(input: List<String>) = input.parse().let { startBlizzards ->
        val start = 0 to 1
        val end = startBlizzards.first().let { it.maxY + 1 to it.maxX }
        val (_, minutes) = startBlizzards.simulate(start, end)
        return@let minutes
    }

    fun part2(input: List<String>) = input.parse().let { startBlizzards ->
        val start = 0 to 1
        val end = startBlizzards.first().let { it.maxY + 1 to it.maxX }
        val (blizzardsFromStartToEnd, minutesFromStartToEnd) = startBlizzards.simulate(start, end)
        val (blizzardsFromEndToStart, minutesFromEndToStart) = blizzardsFromStartToEnd.simulate(end, start)
        val (_, minutesFromStartToEndAgain) = blizzardsFromEndToStart.simulate(start, end)
        return@let minutesFromStartToEnd + minutesFromEndToStart + minutesFromStartToEndAgain
    }

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
