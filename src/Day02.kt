fun main() {

    fun List<String>.parse() = map { it.split(" ") }
            .map { (it.first().first() - 'A') to (it.last().first() - 'X') }

    fun part1(input: List<String>) = input
            .parse()
            .sumOf { (abc, xyz) -> xyz + 1 + 3 * ((4 + xyz - abc) % 3) }

    fun part2(input: List<String>) = input
            .parse()
            .sumOf { (abc, xyz) -> xyz * 3 + 1 + (2 + xyz + abc) % 3 }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
