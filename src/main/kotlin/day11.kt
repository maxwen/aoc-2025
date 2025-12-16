import java.io.File

fun main() {
    println(day11_part1(File(System.getProperty("user.dir"), "input/input_day11.txt").readLines()))
    println(day11_part2(File(System.getProperty("user.dir"), "input/input_day11.txt").readLines()))
}

private val cache = mutableMapOf<String, Long>()

private fun solve(
    tree: Map<String, List<String>>,
    current: String,
    target: String,
    visited: MutableSet<String>,
    cnt: Long
): Long {
    if (current == target) {
        return 1
    }

    var c = cnt

    for (child in tree[current]!!) {
        if (visited.contains(child)) {
            continue
        }
        if (child in cache) {
            c += cache[child]!!
        } else {
            visited.add(child)
            c += solve(tree, child, target, visited, cnt)
            visited.remove(child)
        }
    }
    cache[current] = c

    return c
}

fun day11_part1(lines: List<String>): Long {
    var sum = 0L
    var end = "out"
    var start = "you"
    val tree = mutableMapOf<String, List<String>>()
    for (line in lines) {
        val parts = line.split(": ")
        val node = parts[0]
        val childs = parts[1].trim().split(" ")

        tree[node] = childs
    }
    tree["out"] = listOf<String>()

    val visited = mutableSetOf<String>()
    sum = solve(tree, start, end, visited, 0)
    return sum
}

private fun create_dot_graph(tree: Map<String, List<String>>) {
    val buffer = StringBuffer()

    buffer.append("digraph {")
    for (node in tree.keys) {
        for (child in tree[node]!!) {
            if (child == "dac" || child == "fft") {
                buffer.append(child + " [shape=box];\n")
            }
            buffer.append(node)
            buffer.append(" -> ")
            buffer.append(child)
            buffer.append(";\n")
        }
    }
    buffer.append("}")

    println(buffer)
}

fun day11_part2(lines: List<String>): Long {
    var sum = 0L

    val tree = mutableMapOf<String, List<String>>()
    for (line in lines) {
        val parts = line.split(": ")
        val node = parts[0]
        val childs = parts[1].trim().split(" ")

        tree[node] = childs
    }
    tree["out"] = listOf<String>()

    // 502447498690860
    val visited = mutableSetOf<String>()

    var start = "svr"
    var end = "fft"
    cache.clear()
    visited.clear()

    val svrFftWays = solve(tree, start, end, visited, 0)
    println("svr -> fft " + svrFftWays)

    start = "fft"
    end = "dac"
    cache.clear()
    visited.clear()

    var fftDacWays = solve(tree, start, end, visited, 0)
    println("fft -> dac " + fftDacWays)

    // dac -> fft = 0

    start = "dac"
    end = "out"
    cache.clear()
    visited.clear()

    val dacOutWays = solve(tree, start, end, visited, 0)
    println("dac -> out " + dacOutWays)


    sum = svrFftWays * dacOutWays * fftDacWays
    return sum
}



