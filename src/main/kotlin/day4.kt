import java.io.File
import kotlin.collections.mutableListOf

fun main() {
    day4_part1()
    day4_part2()
}

private val grid = mutableListOf<MutableList<Boolean>>()
private var grid_rows = 0
private var grid_cols = 0

private fun print_map() {
    grid.flatMapIndexed { y, s ->
        s.mapIndexed { x, tile ->
            val pos = x to y
            if (tile) {
                print("@")
            } else {
                print(".")
            }
            if (x == s.size - 1) {
                println()
            }
        }
    }
}

private fun print_possible_map(possible: List<Pair<Int, Int>>) {
    grid.flatMapIndexed { y, s ->
        s.mapIndexed { x, tile ->
            val pos = x to y
            if (pos in possible) {
                print("x")
            } else {
                if (tile) {
                    print("@")
                } else {
                    print(".")
                }
            }
            if (x == s.size - 1) {
                println()
            }
        }
    }
}

private fun pos_inside_map(pos: Pair<Int, Int>): Boolean {
    return pos.first in 0..<grid_cols && pos.second in 0..<grid_rows
}

private fun get_value_at_pos(pos: Pair<Int, Int>): Boolean {
    return grid[pos.second][pos.first]
}

private fun set_value_at_pos(pos: Pair<Int, Int>, v: Boolean) {
    grid[pos.second][pos.first] = v
}


private fun get_possible_grid_neighbours(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(pos.first to pos.second - 1,
        pos.first to pos.second + 1,
        pos.first + 1 to pos.second,
        pos.first - 1 to pos.second,
        pos.first + 1 to pos.second - 1,
        pos.first - 1 to pos.second - 1,
        pos.first + 1 to pos.second + 1,
        pos.first - 1 to pos.second + 1).filter { pos ->  pos_inside_map(pos)}
}

private fun build_grid(lines: List<String>) {
    val grid_lines = mutableListOf<String>()
    for (line in lines) {
        grid_lines.add(line)
    }

    grid_lines.flatMapIndexed { y, s ->
        val line = mutableListOf<Boolean>()
        s.mapIndexed { x, c ->
            val type = when (c) {
                '@' -> true
                else -> false
            }
            line.add(type)

            if (x == s.length - 1) {
                grid.add(line)
            }
        }
    }

    grid_rows = lines.size
    grid_cols = lines[0].length
}

private fun get_possible_positions(): List<Pair<Int, Int>> {
    val possible = mutableListOf<Pair<Int, Int>>()

    grid.flatMapIndexed { y, s ->
        s.mapIndexed { x, tile ->
            val pos = x to y
            var count = 0
            if (get_value_at_pos(pos)) {
                val neighbours = get_possible_grid_neighbours(pos)
                for (n in neighbours) {
                    val v = get_value_at_pos(n)
                    if (v) {
                        count++
                    }
                }
                if (count < 4) {
                    possible += pos
                }
            }
        }
    }
    return possible
}

private fun clear_positions(pos_list: List<Pair<Int, Int>>) {
    for (pos in pos_list) {
        set_value_at_pos(pos, false)
    }
}

fun day4_part1() {
    val lines = File(System.getProperty("user.dir"), "input/input_day4.txt").readLines()
    grid.clear()
    grid_rows = 0
    grid_cols = 0

    build_grid(lines)

//    print_possible_map(possible)
    println(get_possible_positions().size)

}

fun day4_part2() {
    val lines = File(System.getProperty("user.dir"), "input/input_day4.txt").readLines()

    grid.clear()
    grid_rows = 0
    grid_cols = 0

    build_grid(lines)

    var sum = 0
    while (true) {
        val possible = get_possible_positions()
        if (possible.isEmpty()) {
            break
        }
        sum += possible.size
        clear_positions(possible)
    }

    println(sum)
}



