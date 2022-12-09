import kotlin.math.abs

fun main() {

    fun List<String>.parse() = map {
        it.split(" ").let { (direction, steps) ->
            Pair(direction.single(), steps.toInt())
        }
    }

    data class Position(
        val x: Int,
        val y: Int,
    ) {

        fun isAdjacent(position: Position): Boolean {
            val isHorizontallyAdjacent = abs(position.x - x) <= 1
            val isVerticallyAdjacent = abs(position.y - y) <= 1
            return isHorizontallyAdjacent && isVerticallyAdjacent
        }

        fun move(direction: Char) = when (direction) {
            'L' -> copy(x = x - 1)
            'R' -> copy(x = x + 1)
            'U' -> copy(y = y + 1)
            'D' -> copy(y = y - 1)
            else -> this
        }

        fun move(position: Position): Position {
            val horizontalMove = position.x - x
            val verticalMove = position.y - y
            val horizontalDirection = if (horizontalMove == 0) 0 else horizontalMove / abs(horizontalMove)
            val verticalDirection = if (verticalMove == 0) 0 else verticalMove / abs(verticalMove)
            return copy(x = x + horizontalDirection, y = y + verticalDirection)
        }
    }

    fun List<Pair<Char, Int>>.simulate(ropeLength: Int) = let { moves ->
        val rope = MutableList(ropeLength) { Position(x = 0, y = 0) }
        val tailPositions = mutableSetOf<Position>().apply { add(rope.last()) }

        moves.forEach { (direction, steps) ->
            repeat(steps) {
                rope[0] = rope[0].move(direction)
                for (i in 1..rope.lastIndex) {
                    if (!rope[i].isAdjacent(rope[i - 1])) {
                        rope[i] = rope[i].move(rope[i - 1])
                    }
                }
                tailPositions += rope.last()
            }
        }

        return@let tailPositions
    }

    fun part1(input: List<String>) = input.parse().simulate(ropeLength = 2).size

    fun part2(input: List<String>) = input.parse().simulate(ropeLength = 10).size

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
