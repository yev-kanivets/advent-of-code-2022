import kotlin.math.min

sealed class Item : Comparable<Item> {
    data class Values(
        val values: List<Item>
    ) : Item() {

        override fun compareTo(other: Item): Int {
            when (other) {
                is Values -> {
                    for (i in 0..min(values.lastIndex, other.values.lastIndex)) {
                        val result = values[i].compareTo(other.values[i])
                        if (result != 0) return result
                    }
                    return values.lastIndex - other.values.lastIndex
                }

                is Value -> return compareTo(Values(listOf(other)))
            }
        }
    }

    data class Value(
        val value: Int
    ) : Item() {

        override fun compareTo(other: Item): Int = when (other) {
            is Value -> this.value - other.value
            is Values -> Values(listOf(this)).compareTo(other)
        }
    }
}

fun main() {

    fun String.split(): List<String> {
        val items = mutableListOf<String>()

        var depth = 0
        var currentItem = ""

        forEach { char ->
            when (char) {
                '[' -> {
                    currentItem += char
                    depth++
                }

                ']' -> {
                    currentItem += char
                    depth--
                }

                ',' -> if (depth == 0) {
                    items += currentItem
                    currentItem = ""
                } else {
                    currentItem += char
                }

                else -> currentItem += char
            }
        }
        items += currentItem

        return items
    }

    fun String.parse(): Item {
        val content = removeSurrounding(prefix = "[", suffix = "]")
        if (content.isEmpty()) return Item.Values(emptyList())
        if (content.all { it.isDigit() }) return Item.Value(content.toInt())
        return Item.Values(content.split().map { it.parse() })
    }

    fun part1(input: List<String>) = input
        .chunked(3)
        .mapIndexed { index, lines -> Pair(index + 1, lines[0].parse() < lines[1].parse()) }
        .filter { it.second }
        .sumOf { it.first }

    fun part2(input: List<String>) = (input + listOf("[[2]]", "[[6]]"))
        .filter { it.isNotEmpty() }
        .map { it.parse() }
        .sorted()
        .let { packets ->
            val indexOf2 = packets.indexOf(Item.Values(listOf(Item.Value(2)))) + 1
            val indexOf6 = packets.indexOf(Item.Values(listOf(Item.Value(6)))) + 1
            indexOf2 * indexOf6
        }

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
