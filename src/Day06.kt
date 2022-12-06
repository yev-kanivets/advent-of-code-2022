fun main() {

    fun List<String>.findStartMessageMarker(distinctCharacterCount: Int) = first().let { stream ->
        val shift = distinctCharacterCount - 1
        for (index in 0..stream.lastIndex - shift) {
            if (stream.subSequence(index..index + shift).toSet().count() == distinctCharacterCount) {
                return@let index + distinctCharacterCount
            }
        }
    }

    fun part1(input: List<String>) = input.findStartMessageMarker(4)

    fun part2(input: List<String>) = input.findStartMessageMarker(14)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
