import java.util.*

fun main() {

    fun List<String>.parse() = map { line -> line.split(",").map { it.toInt() + 1 /* air margin */ } }
        .map { (x, y, z) -> Triple(x, y, z) }
        .toSet()

    fun Triple<Int, Int, Int>.adjacent() = listOf(
        Triple(first, second, third + 1),
        Triple(first, second, third - 1),
        Triple(first, second + 1, third),
        Triple(first, second - 1, third),
        Triple(first + 1, second, third),
        Triple(first - 1, second, third),
    )

    fun Set<Triple<Int, Int, Int>>.buildAir() = let { droplets ->
        val airSet = mutableSetOf(Triple(0, 0, 0))

        val queue = LinkedList<Triple<Int, Int, Int>>().apply { addAll(airSet) }
        while (queue.isNotEmpty()) {
            val air = queue.poll()
            val adjacentAir = air.adjacent()
                .filter { listOf(it.first, it.second, it.third).all { it in 0..30 } }
                .filterNot { it in airSet }
                .filterNot { it in droplets }
            airSet.addAll(adjacentAir)
            queue.addAll(adjacentAir)
        }

        return@let airSet
    }

    fun part1(input: List<String>) = input.parse().let { droplets ->
        droplets.sumOf { droplet ->
            droplet.adjacent().count { it !in droplets }
        }
    }

    fun part2(input: List<String>) = input.parse().let { droplets ->
        val air = droplets.buildAir()
        droplets.sumOf { droplet ->
            droplet.adjacent().count { it in air }
        }
    }

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
