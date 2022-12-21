import java.lang.IllegalArgumentException

fun main() {

    fun List<String>.parse() = associate { line ->
        line.split(": ").let { (name, operation) ->
            name to operation.split(" ")
        }
    }

    fun Map<String, List<String>>.findValue(monkey: String): Long = let {
        val operation = this[monkey]
        return@let when (operation?.size) {
            1 -> operation.first().toLong()
            3 -> {
                val leftMonkey = findValue(operation[0])
                val rightMonkey = findValue(operation[2])
                when (operation[1]) {
                    "+" -> leftMonkey + rightMonkey
                    "-" -> leftMonkey - rightMonkey
                    "*" -> leftMonkey * rightMonkey
                    "/" -> leftMonkey / rightMonkey
                    else -> 0
                }
            }

            else -> 0
        }
    }

    fun Map<String, List<String>>.containsHuman(monkey: String): Boolean = let {
        if (monkey == "humn") return@let true
        val operation = this[monkey]
        return@let when (operation?.size) {
            1 -> false
            3 -> containsHuman(operation[0]) || containsHuman(operation[2])
            else -> false
        }
    }

    fun Map<String, List<String>>.findHumanValue(monkey: String, value: Long): Long = let {
        if (monkey == "humn") return@let value
        val operation = this[monkey]
        return@let when (operation?.size) {
            3 -> {
                val leftMonkey = findValue(operation[0])
                val rightMonkey = findValue(operation[2])

                if (containsHuman(operation[0])) {
                    when (operation[1]) {
                        "+" -> findHumanValue(operation[0], value - rightMonkey)
                        "-" -> findHumanValue(operation[0], value + rightMonkey)
                        "*" -> findHumanValue(operation[0], value / rightMonkey)
                        "/" -> findHumanValue(operation[0], value * rightMonkey)
                        else -> 0
                    }
                } else {
                    when (operation[1]) {
                        "+" -> findHumanValue(operation[2], value - leftMonkey)
                        "-" -> findHumanValue(operation[2], leftMonkey - value)
                        "*" -> findHumanValue(operation[2], value / leftMonkey)
                        "/" -> findHumanValue(operation[2], value / leftMonkey)
                        else -> 0
                    }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }

    fun part1(input: List<String>) = input.parse().findValue("root")

    fun part2(input: List<String>) = input.parse().let { monkeys ->
        val rootMonkey = monkeys["root"]!!
        val leftMonkey = monkeys.findValue(rootMonkey[0])
        val rightMonkey = monkeys.findValue(rootMonkey[2])

        if (monkeys.containsHuman(rootMonkey[0])) {
            monkeys.findHumanValue(rootMonkey[0], rightMonkey)
        } else {
            monkeys.findHumanValue(rootMonkey[2], leftMonkey)
        }
    }

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
