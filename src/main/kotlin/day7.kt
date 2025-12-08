import java.io.File
import kotlin.collections.mutableListOf

fun main() {
    println(day7_part1(File(System.getProperty("user.dir"), "input/input_day7.txt").readLines()))
    println(day7_part2(File(System.getProperty("user.dir"), "input/input_day7.txt").readLines()))
}

enum class TileType {
    Start, Splitter, Empty, Beam
}

private val grid = mutableListOf<MutableList<TileType>>()
private var grid_rows = 0
private var grid_cols = 0
private var start_pos = 0 to 0


private fun print_map() {
    grid.flatMapIndexed { y, s ->
        s.mapIndexed { x, tile ->
            val pos = x to y
            when (tile) {
                TileType.Start -> print("S")
                TileType.Splitter -> print("^")
                TileType.Empty -> print(".")
                TileType.Beam -> print("|")
            }

            if (x == s.size - 1) {
                println()
            }
        }
    }
}

private fun get_value_at_pos(pos: Pair<Int, Int>): TileType {
    return grid[pos.second][pos.first]
}

private fun set_value_at_pos(pos: Pair<Int, Int>, v: TileType) {
    grid[pos.second][pos.first] = v
}

private fun build_grid(lines: List<String>) {
    val grid_lines = mutableListOf<String>()
    for (line in lines) {
        grid_lines.add(line)
    }

    grid_lines.flatMapIndexed { y, s ->
        val line = mutableListOf<TileType>()
        s.mapIndexed { x, c ->
            val type = when (c) {
                'S' -> {
                    start_pos = x to y
                    TileType.Start
                }

                '^' -> TileType.Splitter
                '.' -> TileType.Empty
                '|' -> TileType.Beam
                else -> TileType.Empty
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

fun day7_part1(lines: List<String>): Int {
    grid.clear()
    grid_rows = 0
    grid_cols = 0

    build_grid(lines)

    var sum = 0

    set_value_at_pos(start_pos.first to 1, TileType.Beam)

    for (y in 2..<grid_rows) {
        for (x in 0..<grid_cols) {
            val pos = x to y
            if (get_value_at_pos(pos.first to y - 1) == TileType.Beam) {
                if (get_value_at_pos(pos) == TileType.Splitter) {
                    // split
                    if (x in 1..<grid_cols - 1) {
                        set_value_at_pos(x - 1 to y, TileType.Beam)
                        set_value_at_pos(x + 1 to y, TileType.Beam)
                        sum += 1
                    }
                }
                // empty pos continue any beam if needed
                if (get_value_at_pos(pos) == TileType.Empty) {
                    set_value_at_pos(pos, TileType.Beam)
                }
            }
        }
    }
    return sum
}

// memoization FTW
private var cache = mutableMapOf<Pair<Int, Int>, Long>()

private fun follow_beam(y: Int, beam_pos: Pair<Int, Int>, cnt: Long): Long {
    if (y == grid_rows - 1) {
        return cnt + 1
    }

    var c = cnt
    for (x in 0..<grid_cols) {
        val pos = x to y
        if (pos.first to y - 1 == beam_pos) {
            if (get_value_at_pos(pos) == TileType.Splitter) {
                if (x in 1..<grid_cols - 1) {
                    if (pos in cache) {
                        c += cache[pos]!!
                    } else {
                        c += follow_beam(y + 1, beam_pos.first - 1 to y, cnt)
                        c += follow_beam(y + 1, beam_pos.first + 1 to y, cnt)
                        cache[pos] = c
                    }
                }
            } else {
                if (pos in cache) {
                    c += cache[pos]!!
                } else {
                    c += follow_beam(y + 1, beam_pos.first to y, cnt)
                    cache[pos] = c
                }
            }
        }
    }
    return c
}

fun day7_part2(lines: List<String>): Long {
    grid.clear()
    grid_rows = 0
    grid_cols = 0

    build_grid(lines)

    val sum = follow_beam(2, start_pos.first to 1, 0L)

    return sum
}



