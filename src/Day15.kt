import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun String.parse() = split(",")
        .map { it.split("=").last().trim().toLong() }
        .let { (x, y) -> Pair(x, y) }

    fun List<String>.parse() = map { line ->
        line.split(":").let { (sensorString, beaconString) ->
            val sensor = sensorString.removePrefix("Sensor at ").parse()
            val beacon = beaconString.removePrefix(" closest beacon is at ").parse()
            Pair(sensor, beacon)
        }
    }

    fun Pair<Long, Long>.distance(to: Pair<Long, Long>) = abs(first - to.first) + abs(second - to.second)

    fun Pair<Pair<Long, Long>, Pair<Long, Long>>.projection(y: Long): LongRange? {
        val distance = first.distance(to = second)
        val verticalDistance = abs(y - first.second)
        val projectedDistance = distance - verticalDistance
        return if (verticalDistance > distance) null
        else (first.first - projectedDistance)..(first.first + projectedDistance)
    }

    fun LongRange.intersection(range: LongRange): LongRange? {
        val start = max(first, range.first)
        val end = min(last, range.last)
        return if (start <= end) start..end else null
    }

    fun LongRange.combination(range: LongRange) = if (intersection(range) == null && last + 1 != range.first) {
        null
    } else {
        min(first, range.first)..max(last, range.last)
    }

    fun List<Pair<Pair<Long, Long>, Pair<Long, Long>>>.projection(y: Long) = mapNotNull { it.projection(y) }
        .sortedBy { it.first }
        .let { projections ->
            val mergedProjections = mutableListOf<LongRange>()
            projections.forEach { projection ->
                val mergeIndex = mergedProjections.indexOfFirst { it.combination(projection) != null }
                if (mergeIndex == -1) mergedProjections += projection
                else mergedProjections[mergeIndex] = mergedProjections[mergeIndex].combination(projection)!!
            }
            mergedProjections
        }

    fun List<Pair<Pair<Long, Long>, Pair<Long, Long>>>.beacons(
        x: LongRange,
        y: LongRange
    ) = map { it.second }.distinct().filter { beacon -> beacon.first in x && beacon.second in y }

    fun part1(input: List<String>) = input.parse().let { pairs ->
        val y = 2000000L
        val projections = pairs.projection(y)
        val beacons = projections.flatMap { pairs.beacons(it, y..y) }
        projections.sumOf { it.count() } - beacons.size
    }

    fun part2(input: List<String>) = input.parse().let { pairs ->
        val range = 0L..4000000L
        for (y in range) {
            val projections = pairs.projection(y).mapNotNull { it.intersection(range) }
            if (projections.isNotEmpty()) {
                if (projections.first().first != 0L) return@let y
                if (projections.last().last != range.last) return@let range.last * range.last + y
                if (projections.size == 2) return@let (projections.first().last + 1) * range.last + y
            }
        }
    }

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
