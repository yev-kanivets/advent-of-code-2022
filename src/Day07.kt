fun main() {

    data class File(
        val name: String,
        private var size: Long,
        val parent: File?,
        val files: MutableList<File> = mutableListOf()
    ) {

        fun cd(root: File, destination: String) = when (destination) {
            "/" -> root
            ".." -> parent
            else -> files.find { it.name == destination }
        }

        fun size(): Long {
            if (size == 0L) size = files.sumOf { it.size() }
            return size
        }
    }

    fun List<String>.parse() = let { lines ->
        val root = File(
            name = "/",
            size = 0L,
            parent = null,
        )
        var current = root

        lines.forEach { line ->
            val args = line.split(" ")
            when (args[0]) {
                "$" -> when (args[1]) {
                    "cd" -> current = current.cd(root = root, destination = args[2])!!
                    "ls" -> Unit
                }
                "dir" -> current.files.add(File(name = args[1], size = 0L, parent = current))
                else -> current.files.add(File(name = args[1], size = args[0].toLong(), parent = current))
            }
        }

        root.apply { size() }
    }

    fun File.directorySizes(): List<Long> {
        val directorySizes = mutableListOf<Long>()
        val directories = mutableListOf<File>().apply { add(this@directorySizes) }
        while (directories.isNotEmpty()) {
            val directory = directories.removeLast()
            directorySizes.add(directory.size())
            directories.addAll(directory.files.filter { it.files.isNotEmpty() })
        }
        return directorySizes
    }

    fun part1(input: List<String>) = input.parse().directorySizes().filter { it <= 100000 }.sum()

    fun part2(input: List<String>) = input.parse().directorySizes().sorted().let { sizes ->
        val remainingSpace = 70000000 - sizes.last()
        val spaceToFree = 30000000 - remainingSpace

        return@let if (spaceToFree <= 0) 0
        else sizes.first { it >= spaceToFree }
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
