import kotlin.math.max

fun main() {

    data class Step(
        val ore: Int,
        val clay: Int,
        val obsidian: Int,
        val geode: Int,
        val oreR: Int,
        val clayR: Int,
        val obsidianR: Int,
        val geodeR: Int,
    ) {

        fun next() = copy(
            ore = ore + oreR,
            clay = clay + clayR,
            obsidian = obsidian + obsidianR,
            geode = geode + geodeR,
        )
    }

    data class Blueprint(
        val id: Int,
        val ore: List<Int>,
        val clay: List<Int>,
        val obsidian: List<Int>,
        val geode: List<Int>,
    ) {

        fun simulate(minutes: Int): Int {
            val maxOre = listOf(ore[0], clay[0], obsidian[0], geode[0]).maxOf { it }

            var maxGeode = 0
            var maxGeodeR = 0
            var steps = setOf(Step(0, 0, 0, 0, 1, 0, 0, 0))

            repeat(minutes) {
                val nextSteps = mutableSetOf<Step>()
                steps.forEach { step ->
                    maxGeode = max(maxGeode, step.geode + step.geodeR)
                    maxGeodeR = max(maxGeodeR, step.geodeR)

                    val nextStep = step.next()
                    if (step.obsidian >= geode[2] && step.ore >= geode[0]) {
                        nextSteps += nextStep.copy(
                            ore = nextStep.ore - geode[0],
                            obsidian = nextStep.obsidian - geode[2],
                            geodeR = nextStep.geodeR + 1,
                        )
                    }
                    if (step.obsidianR < geode[2] && step.clay >= obsidian[1] && step.ore >= obsidian[0]) {
                        nextSteps += nextStep.copy(
                            ore = nextStep.ore - obsidian[0],
                            clay = nextStep.clay - obsidian[1],
                            obsidianR = nextStep.obsidianR + 1,
                        )
                    }
                    if (step.clayR < obsidian[1] && step.ore >= clay[0]) {
                        nextSteps += nextStep.copy(
                            ore = nextStep.ore - clay[0],
                            clayR = nextStep.clayR + 1,
                        )
                    }
                    if (step.oreR < maxOre && step.ore >= ore[0]) {
                        nextSteps += nextStep.copy(
                            ore = nextStep.ore - ore[0],
                            oreR = nextStep.oreR + 1,
                        )
                    }
                    if (step.ore <= maxOre) {
                        nextSteps += nextStep
                    }
                }
                steps = nextSteps.filter { it.geodeR >= maxGeodeR }.toSet()
            }

            return maxGeode
        }
    }

    fun List<String>.parse(resource: String) = firstOrNull { it.contains(resource) }?.split(" ")?.first()?.toInt() ?: 0

    fun List<String>.parse() = map { line ->
        line.removePrefix("Blueprint ").split(": ").let { (id, content) ->
            val robots = content.split(".").filter { it.isNotEmpty() }.map { robot ->
                val cost = robot.split("costs ").last().split(" and ")
                listOf(
                    cost.parse(resource = "ore"),
                    cost.parse(resource = "clay"),
                    cost.parse(resource = "obsidian"),
                )
            }
            Blueprint(
                id = id.toInt(),
                ore = robots[0],
                clay = robots[1],
                obsidian = robots[2],
                geode = robots[3],
            )
        }
    }

    fun part1(input: List<String>) = input.parse().sumOf { blueprint ->
        blueprint.id * blueprint.simulate(24)
    }

    fun part2(input: List<String>) = input.parse()
        .take(3)
        .map { it.simulate(32) }
        .reduce { acc, geodes -> acc * geodes }

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
