import java.lang.IllegalArgumentException
import kotlin.math.max

fun main() {

    fun List<String>.parse() = first()

    fun Array<IntArray>.print() = joinToString("\n") { it.joinToString("") { it.toString() } }

    fun buildRock(id: Int): Array<IntArray> {
        fun buildHBar() = Array(1) { IntArray(7) { 0 } }.apply {
            this[0][2] = 1
            this[0][3] = 1
            this[0][4] = 1
            this[0][5] = 1
        }

        fun buildPlus() = Array(3) { IntArray(7) { 0 } }.apply {
            this[0][3] = 2
            this[1][2] = 2
            this[1][3] = 2
            this[1][4] = 2
            this[2][3] = 2
        }

        fun buildCorner() = Array(3) { IntArray(7) { 0 } }.apply {
            this[0][4] = 3
            this[1][4] = 3
            this[2][2] = 3
            this[2][3] = 3
            this[2][4] = 3
        }

        fun buildVBar() = Array(4) { IntArray(7) { 0 } }.apply {
            this[0][2] = 4
            this[1][2] = 4
            this[2][2] = 4
            this[3][2] = 4
        }

        fun buildSquare() = Array(2) { IntArray(7) { 0 } }.apply {
            this[0][2] = 5
            this[0][3] = 5
            this[1][2] = 5
            this[1][3] = 5
        }

        fun buildEmpty() = Array(0) { IntArray(0) }

        return when (id % 5) {
            0 -> buildHBar()
            1 -> buildPlus()
            2 -> buildCorner()
            3 -> buildVBar()
            4 -> buildSquare()
            else -> buildEmpty()
        }
    }

    fun Array<IntArray>.moveLeftIfPossible(tetris: Array<IntArray>, y: Int) {
        var isMovePossible = all { it.first() == 0 }
        for (i in 0..lastIndex) {
            for (j in 1..first().lastIndex) {
                if (this[i][j] != 0 && tetris[y - lastIndex + i][j - 1] != 0) {
                    isMovePossible = false
                }
            }
        }
        if (isMovePossible) {
            for (i in 0..lastIndex) {
                for (j in 1..first().lastIndex) {
                    this[i][j - 1] = this[i][j]
                    this[i][j] = 0
                }
            }
        }
    }

    fun Array<IntArray>.moveRightIfPossible(tetris: Array<IntArray>, y: Int) {
        var isMovePossible = all { it.last() == 0 }
        for (i in 0..lastIndex) {
            for (j in 0 until first().lastIndex) {
                if (this[i][j] != 0 && tetris[y - lastIndex + i][j + 1] != 0) {
                    isMovePossible = false
                }
            }
        }
        if (isMovePossible) {
            for (i in 0..lastIndex) {
                for (j in first().lastIndex - 1 downTo 0) {
                    this[i][j + 1] = this[i][j]
                    this[i][j] = 0
                }
            }
        }
    }

    fun Array<IntArray>.isMoveDownPossible(tetris: Array<IntArray>, y: Int): Boolean {
        for (i in indices) {
            for (j in first().indices) {
                if (this[i][j] != 0 && tetris[y - lastIndex + i + 1][j] != 0) {
                    return false
                }
            }
        }
        return true
    }

    fun String.simulate(rocks: Int) = let { pattern ->
        var tetris = Array(7) { IntArray(7) { 0 } }
        var height = 0
        var moves = 0

        repeat(rocks) { id ->
            val rock = buildRock(id)
            var y = tetris.lastIndex - height - 3

            while (true) {
                when (pattern[moves % pattern.count()]) {
                    '<' -> rock.moveLeftIfPossible(tetris, y)
                    '>' -> rock.moveRightIfPossible(tetris, y)
                }

                moves++

                if (y == tetris.lastIndex) break
                if (rock.isMoveDownPossible(tetris, y)) y++ else break
            }

            for (i in rock.indices) {
                for (j in rock.first().indices) {
                    if (rock[i][j] != 0) tetris[y - rock.lastIndex + i][j] = rock[i][j]
                }
            }

            val newHeight = max(height, tetris.lastIndex - y + rock.size)
            tetris = Array(newHeight - height) { IntArray(7) { 0 } } + tetris
            height = newHeight
        }

        return@let tetris.reversedArray().dropLast(7).toTypedArray()
    }

    fun Array<IntArray>.findCycle(startRange: IntRange) = let { tetris ->
        for (start in startRange) {
            for (shift in 1..tetris.lastIndex / 2) {
                for (i in 0..tetris.lastIndex - shift - start) {
                    if (tetris[start + i].contentEquals(tetris[start + shift + i])) {
                        if (i > 20) return@let Pair(start, shift)
                    } else {
                        break
                    }
                }
            }
        }
        throw IllegalArgumentException("Insufficient startRange")
    }

    fun part1(input: List<String>) = input.parse().simulate(rocks = 10000).size

    fun part2(input: List<String>) = input.parse().let { pattern ->
        val rocks = 1000000000000
        val tetris = pattern.simulate(10000)
        val (start, shift) = tetris.findCycle(startRange = 0..200)

        val startRocks = 71 // pattern.simulate(71)
        val cycleRocks = 1700 // pattern.simulate(1771)
        val cyclesHeight = (rocks - startRocks) / cycleRocks * shift
        val remainder = (rocks - startRocks) % cycleRocks
        val remainderHeight = pattern.simulate(startRocks + remainder.toInt()).size - start

        start + cyclesHeight + remainderHeight
    }

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
