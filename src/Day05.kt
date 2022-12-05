fun main() {

    fun List<String>.parse() = indexOf("").let { dividerIndex ->
        val stackCount = this[dividerIndex - 1].split(' ').filterNot { it.isBlank() }.count()
        val stacks = List<MutableList<Char>>(stackCount) { mutableListOf() }

        subList(0, dividerIndex - 1).reversed().forEach { row ->
            stacks.forEachIndexed { index, stack ->
                val containerPosition = index * 4 + 1
                if (containerPosition in row.indices) {
                    row[containerPosition].takeIf { it.isUpperCase() }?.let {
                        stack += it
                    }
                }
            }
        }

        val commands = subList(dividerIndex + 1, lastIndex + 1).map { command ->
            val numbers = command.filterNot { it.isLetter() }
                    .split(" ")
                    .filterNot { it.isBlank() }
                    .map { it.toInt() }
            Triple(numbers[0], numbers[1], numbers[2])
        }

        Pair(stacks, commands)
    }

    fun part1(input: List<String>) = input.parse().let { (stacks, commands) ->
        commands.forEach { (count, from, to) ->
            repeat(count) {
                stacks[to - 1].add(stacks[from - 1].removeLast())
            }
        }
        stacks.joinToString(separator = "") { it.last().toString() }
    }

    fun part2(input: List<String>) = input.parse().let { (stacks, commands) ->
        commands.forEach { (count, from, to) ->
            val containers = stacks[from - 1].takeLast(count)
            repeat(count) { stacks[from - 1].removeLast() }
            stacks[to - 1].addAll(containers)
        }
        stacks.joinToString(separator = "") { it.last().toString() }
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
