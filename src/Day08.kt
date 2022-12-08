import kotlin.math.max

fun main() {

    fun List<String>.parse() = map { row -> row.map { it.digitToInt() } }

    data class View(
        val left: List<Int>,
        val right: List<Int>,
        val top: List<Int>,
        val bottom: List<Int>,
    ) {

        val directions = listOf(left, right, top, bottom)
    }

    fun List<List<Int>>.buildView(i: Int, j: Int) = View(
        left = this[i].subList(0, j).reversed(),
        right = this[i].subList(j + 1, this[i].lastIndex + 1),
        top = this.subList(0, i).map { it[j] }.reversed(),
        bottom = this.subList(i + 1, this.lastIndex + 1).map { it[j] },
    )

    fun part1(input: List<String>) = input.parse().let { forest ->
        val height = forest.size
        val width = forest.first().size

        var visibleTreeCount = 2 * (height + width - 2)

        for (i in 1 until forest.lastIndex) {
            for (j in 1 until forest[i].lastIndex) {
                val view = forest.buildView(i, j)
                val isTreeVisible = view.directions
                    .map { direction -> direction.maxOf { it } }
                    .any { it < forest[i][j] }
                if (isTreeVisible) visibleTreeCount += 1
            }
        }

        visibleTreeCount
    }

    fun part2(input: List<String>) = input.parse().let { forest ->
        var maxScenicScore = 0

        for (i in 1 until forest.lastIndex) {
            for (j in 1 until forest[i].lastIndex) {
                val view = forest.buildView(i, j)
                val scenicScore = view.directions
                    .map { direction ->
                        val index = direction.indexOfFirst { it >= forest[i][j] } + 1
                        index.takeIf { it != 0 } ?: direction.size
                    }
                    .reduce { acc, index -> acc * index }
                maxScenicScore = max(maxScenicScore, scenicScore)
            }
        }

        maxScenicScore
    }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
