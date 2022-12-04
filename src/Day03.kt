fun main() {

    fun String.unpack() = map { item ->
        if (item.code >= 'a'.code) {
            item.code - 'a'.code + 1
        } else {
            item.code - 'A'.code + 27
        }
    }

    fun List<String>.parse() = map { it.unpack() }

    fun part1(input: List<String>) = input
            .parse()
            .map { it.chunked(it.size / 2) }
            .sumOf { (first, second) ->
                first.filter { second.contains(it) }.distinct().sum()
            }

    fun part2(input: List<String>) = input
            .parse()
            .chunked(3)
            .sumOf { (first, second, third) ->
                first.filter { second.contains(it) && third.contains(it) }.distinct().sum()
            }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
