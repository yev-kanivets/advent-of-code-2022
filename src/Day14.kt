import kotlin.math.max
import kotlin.math.min

fun main() {

    var xShift = 0
    val margin = 250

    fun List<String>.parse() = map { line ->
        line.split(" -> ").map { pair ->
            pair.split(",").let { (first, second) ->
                Pair(first.toInt(), second.toInt())
            }
        }
    }

    fun List<List<Pair<Int, Int>>>.normalize() = let { rocks ->
        xShift = rocks.map { rock -> rock.map { it.first } }.flatten().minOf { it } - margin
        rocks.map { rock -> rock.map { Pair(it.first - xShift, it.second) } }
    }

    fun List<List<Pair<Int, Int>>>.buildMap() = let { rocks ->
        val maxX = rocks.map { rock -> rock.map { it.first } }.flatten().maxOf { it } + margin
        val maxY = rocks.map { rock -> rock.map { it.second } }.flatten().maxOf { it } + margin

        val map = Array(maxY) { IntArray(maxX) { 0 } }
        rocks.forEach { rock ->
            var previousPoint = rock.first()
            rock.forEach { point ->
                for (i in min(previousPoint.second, point.second)..max(previousPoint.second, point.second)) {
                    for (j in min(previousPoint.first, point.first)..max(previousPoint.first, point.first)) {
                        map[i][j] = 1
                    }
                }
                previousPoint = point
            }
        }
        return@let map
    }

    fun Array<IntArray>.addFloor(): Array<IntArray> {
        for (x in 0..first().lastIndex) {
            this[size - margin + 2][x] = 1
        }
        return this
    }

    fun Array<IntArray>.simulate(): Array<IntArray> {
        while (true) {
            var sandX = 500 - xShift
            var sandY = 0

            while (true) {
                when {
                    this[sandY + 1][sandX] == 0 -> {
                        sandY += 1
                    }

                    this[sandY + 1][sandX - 1] == 0 -> {
                        sandY += 1
                        sandX -= 1
                    }

                    this[sandY + 1][sandX + 1] == 0 -> {
                        sandY += 1
                        sandX += 1
                    }

                    else -> {
                        this[sandY][sandX] = 2
                        break
                    }
                }
                if (sandY == lastIndex || sandX == 0 || sandX == first().lastIndex) return this
            }

            if (sandY == 0 && sandX == 500 - xShift) return this
        }
    }

    fun part1(input: List<String>) = input.parse()
        .normalize()
        .buildMap()
        .simulate()
        .sumOf { row -> row.count { it == 2 } }

    fun part2(input: List<String>) = input.parse()
        .normalize()
        .buildMap()
        .addFloor()
        .simulate()
        .sumOf { row -> row.count { it == 2 } }

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
