import kotlin.math.min

const val INF = 1000000000
const val MAX_VALVES = 11
fun main() {

    fun List<String>.parse() = map { line ->
        val (valveString, tunnelsString) = line.split(";").map { it.trim() }
        val valveName = valveString.substring(6..7)
        val flowRate = valveString.substringAfter("=").toInt()
        val tunnels = if (tunnelsString.contains(",")) {
            tunnelsString.substringAfter("tunnels lead to valves ").split(",").map { it.trim() }
        } else {
            listOf(tunnelsString.substringAfter("tunnel leads to valve "))
        }
        valveName to Pair(flowRate, tunnels)
    }

    fun List<Pair<String, Pair<Int, List<String>>>>.normalize() = let { valves ->
        val names = valves.sortedBy { it.first }.mapIndexed { index, valve -> valve.first to index }.toMap()
        valves.map { valve ->
            names[valve.first]!! to Pair(valve.second.first, valve.second.second.map { names[it]!! }.sorted())
        }.sortedBy { it.first }
    }

    fun List<Pair<Int, Pair<Int, List<Int>>>>.toGrid() = let { valves ->
        val grid = Array(size) { i -> IntArray(size) { j -> if (i == j) 0 else INF } }

        valves.forEach { valve ->
            valve.second.second.forEach { tunnel ->
                grid[valve.first][tunnel] = 1
            }
        }

        grid
    }

    fun Array<IntArray>.shortestPaths() = let { d ->
        for (k in 0..d.lastIndex) {
            for (i in 0..d.lastIndex) {
                for (j in 0..d.lastIndex) {
                    d[i][j] = min(d[i][j], d[i][k] + d[k][j])
                }
            }
        }
        return@let d
    }

    fun <T> List<T>.permutations(): List<List<T>> = if (isEmpty()) {
        listOf(emptyList())
    } else {
        mutableListOf<List<T>>().also { result ->
            forEach { element ->
                (this - element).permutations().forEach { permutation ->
                    result.add(permutation + element)
                }
            }
        }
    }

    fun List<Int>.combinations(k: Int): List<List<Int>> = if (k == 0) {
        listOf(emptyList())
    } else {
        mutableListOf<List<Int>>().also { result ->
            forEach { element ->
                (this - element).combinations(k - 1).forEach { combination ->
                    result.add(combination + element)
                }
            }
        }.map { it.sorted() }.distinct()
    }

    val input = readInput("Day16")
    val valves = input.parse().normalize()
    val shortestPaths = valves.toGrid().shortestPaths()
    val nonZeroValves = valves.filterNot { it.second.first == 0 }.map { it.first }

    fun List<Int>.solve(timeAvailable: Int) = permutations().maxOf { permutation ->
        var prev = 0
        var sum = 0
        var rate = 0
        var timeLeft = timeAvailable

        permutation.forEach {
            val moveTime = shortestPaths[prev][it] + 1
            if (timeLeft >= moveTime) {
                prev = it
                timeLeft -= moveTime
                sum += moveTime * rate
                rate += valves[it].second.first
            }
        }

        sum + timeLeft * rate
    }

    fun part1() = nonZeroValves
        .combinations(min(8, nonZeroValves.size))
        .maxOf { combination -> combination.solve(30) }

    fun part2() = nonZeroValves
        .combinations(nonZeroValves.size / 2)
        .maxOf { combination -> (nonZeroValves - combination).solve(26) + combination.solve(26) }

    println(part1())
    println(part2())
}
