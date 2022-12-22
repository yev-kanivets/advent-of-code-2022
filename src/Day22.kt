import kotlin.math.min

fun main() {

    fun String.parse() = let { input ->
        var route = input

        val result = mutableListOf<String>()
        while (route.isNotEmpty()) {
            val nextTurnIndex = min(
                route.indexOf('R').takeIf { it != -1 } ?: route.lastIndex,
                route.indexOf('L').takeIf { it != -1 } ?: route.lastIndex
            )
            result += route.substring(0 until nextTurnIndex)
            result += route[nextTurnIndex].toString()
            route = route.substring(nextTurnIndex + 1..route.lastIndex)
        }

        return@let result.dropLast(1)
    }

    fun List<String>.parse() = let { input ->
        input.subList(0, input.lastIndex - 1) to (input.last() + 'R').parse()
    }

    fun part1(input: List<String>) = input.parse().let { (map, route) ->
        var direction = 0
        var x = map.first().indexOf('.')
        var y = 0
        var i = 0

        while (i <= route.lastIndex) {
            for (j in 1..route[i].toInt()) {
                when (direction) {
                    0 -> if (x + 1 <= map[y].lastIndex) {
                        if (map[y][x + 1] == '.') x++
                    } else {
                        val leftIndex = map[y].indexOfFirst { it in listOf('.', '#') }
                        if (map[y][leftIndex] == '.') x = leftIndex
                    }

                    1 -> if (y + 1 <= map.lastIndex && x <= map[y + 1].lastIndex) {
                        if (map[y + 1][x] == '.') y++
                        else if (map[y + 1][x] == ' ') {
                            val topIndex = map.indexOfFirst { x <= map[y + 1].lastIndex && it[x] in listOf('.', '#') }
                            if (map[topIndex][x] == '.') y = topIndex
                        }
                    } else {
                        val topIndex = map.indexOfFirst { it[x] in listOf('.', '#') }
                        if (map[topIndex][x] == '.') y = topIndex
                    }

                    2 -> if (x - 1 >= 0) {
                        if (map[y][x - 1] == '.') x--
                        else if (map[y][x - 1] == ' ') {
                            val rightIndex = map[y].indexOfLast { it in listOf('.', '#') }
                            if (map[y][rightIndex] == '.') x = rightIndex
                        }
                    } else {
                        val rightIndex = map[y].indexOfLast { it in listOf('.', '#') }
                        if (map[y][rightIndex] == '.') x = rightIndex
                    }

                    3 -> if (y - 1 >= 0 && x <= map[y - 1].lastIndex) {
                        if (map[y - 1][x] == '.') y--
                        else if (map[y - 1][x] == ' ') {
                            val downIndex = map.indexOfLast { x <= it.lastIndex && it[x] in listOf('.', '#') }
                            if (map[downIndex][x] == '.') y = downIndex
                        }
                    } else {
                        val downIndex = map.indexOfLast { x <= it.lastIndex && it[x] in listOf('.', '#') }
                        if (map[downIndex][x] == '.') y = downIndex
                    }
                }
            }
            if (i != route.lastIndex) {
                direction = (4 + direction + if (route[i + 1] == "R") 1 else -1) % 4
                i++
            }
            i++
        }

        1000 * (y + 1) + 4 * (x + 1) + direction
    }

    fun part2(input: List<String>) = input.parse().let { (map, route) ->
        var direction = 0
        var x = map.first().indexOf('.')
        var y = 0
        var i = 0

        val side = map.size / 4
        val planes = listOf(
            (side until 2 * side) to (0 until side), // 0
            (2 * side until 3 * side) to (0 until side), // 1
            (side until 2 * side) to (side until 2 * side), // 2
            (side until 2 * side) to (2 * side until 3 * side), // 3
            (0 until side) to (2 * side until 3 * side), // 4
            (0 until side) to (3 * side until 4 * side) // 5
        )

        while (i <= route.lastIndex) {
            for (j in 1..route[i].toInt()) {
                val planeIndex = planes.indexOfFirst { x in it.first && y in it.second }
                when (direction) {
                    0 -> if (x + 1 in planes[planeIndex].first) {
                        if (map[y][x + 1] == '.') x++
                    } else {
                        when (planeIndex) {
                            0 -> if (map[y][x + 1] == '.') x++ // 1
                            1 -> if (map[3 * side - y - 1][2 * side - 1] == '.') { // 3
                                y = 3 * side - y - 1
                                x = 2 * side - 1
                                direction = 2
                            }
                            2 -> if (map[side - 1][2 * side + (y - side)] == '.') { // 1
                                x = 2 * side + (y - side)
                                y = side - 1
                                direction = 3
                            }
                            3 -> if (map[side - (y - 2 * side) - 1][3 * side - 1] == '.') { // 1
                                y = side - (y - 2 * side) - 1
                                x = 3 * side - 1
                                direction = 2
                            }
                            4 -> if (map[y][x + 1] == '.') x++ // 3
                            5 -> if (map[3 * side - 1][side + (y - 3 * side)] == '.') { // 3
                                x = side + (y - 3 * side)
                                y = 3 * side - 1
                                direction = 3
                            }
                        }
                    }

                    1 -> if (y + 1 in planes[planeIndex].second) {
                        if (map[y + 1][x] == '.') y++
                    } else {
                        when (planeIndex) {
                            0 -> if (map[y + 1][x] == '.') y++ // 2
                            1 -> if (map[side + (x - 2 * side)][2 * side - 1] == '.') { // 2
                                y = side + (x - 2 * side)
                                x = 2 * side - 1
                                direction = 2
                            }
                            2 -> if (map[y + 1][x] == '.') y++ // 3
                            3 -> if (map[3 * side + (x - side)][side - 1] == '.') { // 5
                                y = 3 * side + (x - side)
                                x = side - 1
                                direction = 2
                            }
                            4 -> if (map[y + 1][x] == '.') y++ // 5
                            5 -> if (map[0][2 * side + x] == '.') { // 1
                                x += 2 * side
                                y = 0
                                direction = 1
                            }
                        }
                    }

                    2 -> if (x - 1 in planes[planeIndex].first) {
                        if (map[y][x - 1] == '.') x--
                    } else {
                        when (planeIndex) {
                            0 -> if (map[3 * side - y - 1][0] == '.') { // 4
                                y = 3 * side - y - 1
                                x = 0
                                direction = 0
                            }
                            1 -> if (map[y][x - 1] == '.') x-- // 0
                            2 -> if (map[2 * side][y - side] == '.') { // 4
                                x = y - side
                                y = 2 * side
                                direction = 1
                            }
                            3 -> if (map[y][x - 1] == '.') x-- // 4
                            4 -> if (map[3 * side - y - 1][side] == '.') { // 0
                                y = 3 * side - y - 1
                                x = side
                                direction = 0
                            }
                            5 -> if (map[0][side + (y - 3 * side)] == '.') { // 0
                                x = side + (y - 3 * side)
                                y = 0
                                direction = 1
                            }
                        }
                    }

                    3 -> if (y - 1 in planes[planeIndex].second) {
                        if (map[y - 1][x] == '.') y--
                    } else {
                        when (planeIndex) {
                            0 -> if (map[3 * side + (x - side)][0] == '.') { // 5
                                y = 3 * side + (x - side)
                                x = 0
                                direction = 0
                            }
                            1 -> if (map[4 * side - 1][x - 2 * side] == '.') { // 5
                                x -= 2 * side
                                y = 4 * side - 1
                                direction = 3
                            }
                            2 -> if (map[y - 1][x] == '.') y-- // 0
                            3 -> if (map[y - 1][x] == '.') y-- // 2
                            4 -> if (map[side + x][side] == '.') { // 2
                                y = side + x
                                x = side
                                direction = 0
                            }
                            5 -> if (map[y - 1][x] == '.') y-- // 4
                        }
                    }
                }
            }
            if (i != route.lastIndex) {
                direction = (4 + direction + if (route[i + 1] == "R") 1 else -1) % 4
                i++
            }
            i++
        }

        1000 * (y + 1) + 4 * (x + 1) + direction
    }

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
