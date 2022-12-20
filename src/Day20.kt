fun main() {

    fun List<String>.parse() = mapIndexed { index, value -> Pair(value.toLong(), index) }

    fun List<Pair<Long, Int>>.move() = toMutableList().let { list ->
        var index = list.indexOfFirst { it.second == 0 }
        while (index != -1) {
            val item = list[index]
            list.removeAt(index)
            val newIndex = ((1_000_000_000_000L * list.size + index + item.first) % list.size).toInt()
            list.add(newIndex, item)
            index = list.indexOfFirst { it.second == item.second + 1 }
        }
        return@let list
    }

    fun List<Pair<Long, Int>>.calculateCoordinate() = map { it.first }.let { list ->
        val zeroIndex = list.indexOf(0)
        return@let (1..3).sumOf { i -> list[(zeroIndex + i * 1000) % list.size] }
    }

    fun part1(input: List<String>) = input.parse()
        .move()
        .calculateCoordinate()

    fun part2(input: List<String>) = input.parse()
        .map { it.copy(first = it.first * 811589153) }
        .let {
            var list = it
            repeat(10) {
                list = list.move()
            }
            list
        }.calculateCoordinate()

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
