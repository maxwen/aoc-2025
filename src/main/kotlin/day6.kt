import java.io.File

fun main() {
    println(day6_part1(File(System.getProperty("user.dir"), "input/input_day6.txt").readLines()))
    println(day6_part2(File(System.getProperty("user.dir"), "input/input_day6.txt").readLines()))
}

fun day6_part1(lines: List<String>): Long {
    val cols = lines[0].trim().split(" ").filter { c -> !c.isBlank() }.size
    val ops = lines.last().trim().split(" ").filter { c -> !c.isBlank() }

    val grid = mutableListOf<Array<Long?>>()

    // create grid of the values for x,y access
    lines.subList(0, lines.size - 1).flatMapIndexed { y, line ->
        val lineParts = line.trim().split(" ").filter { c -> !c.isBlank() }
        grid.add(arrayOfNulls<Long>(cols))
        lineParts.mapIndexed { x, v ->
            val value = v.toLong()
            grid[y][x] = value
        }
    }

    var sum = 0L
    ops.mapIndexed { x, op ->
        var res = 0L
        if (op == "*") {
            res = grid[0][x]!!
            for (y in 1..<grid.size) {
                res *= grid[y][x]!!
            }
        }
        if (op == "+") {
            res = grid[0][x]!!
            for (y in 1..<grid.size) {
                res += grid[y][x]!!
            }
        }
        sum += res
    }

    return sum
}

fun day6_part2(lines: List<String>): Long {
    val grid = mutableListOf<String>()

    // just put it into a grid so we can have x,y access of every char
    lines.subList(0, lines.size).mapIndexed { y, line ->
        grid.add(line)
    }
    val opsLine = lines.last()

    var sum = 0L
    // 0 == + 1 == *
    var op = 0
    var lastCalc = 0L

    for (x in 0..< opsLine.length) {
        // a new op started
        if (opsLine[x] != ' ') {
            sum += lastCalc
            lastCalc = 0
            if (opsLine[x] == '*') {
                op = 1
            }
            if (opsLine[x] == '+') {
                op = 0
            }
        }
        // get num chars from grid on this col
        var numString = ""
        for (y in 0..<grid.size - 1) {
            val n = grid[y][x]
            numString += n
        }
        // separator col
        if (numString.isBlank()) {
            continue
        }
        val num = numString.trim().toLong()
        if (lastCalc == 0L) {
            lastCalc = num
        } else {
            if (op == 0) {
                lastCalc += num
            }
            if (op == 1) {
                lastCalc *= num
            }
        }
    }
    // last op ended when end of line
    sum += lastCalc

    return sum
}



