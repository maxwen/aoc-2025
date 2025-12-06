import java.io.File

fun main() {
    println(day1_part1(File(System.getProperty("user.dir"), "input/input_day1.txt").readLines()))
    println(day1_part2(File(System.getProperty("user.dir"), "input/input_day1.txt").readLines()))
}

fun day1_part1(lines: List<String>): Int {
    val regex = Regex("(?<direction>[LR])(?<clicks>[0-9]+)")

    var pos = 50
    var numZero = 0

    for (line in lines) {
        val matchResult = regex.find(line)!!
        val direction = matchResult.groups["direction"]?.value
        val clicks = matchResult.groups["clicks"]?.value?.toInt()!!

        if (direction == "L") {
            pos = (pos - clicks) % 100
            if (pos < 0) {
                pos += 100
            }
        } else if (direction == "R") {
            pos = (pos + clicks) % 100
        }

        if (pos == 0) {
            numZero += 1
        }
    }
    return numZero
}

fun day1_part2(lines: List<String>) : Int{
    val regex = Regex("(?<direction>[LR])(?<clicks>[0-9]+)")

    var pos = 50
    var numZero = 0
    var startZero = false

    for (line in lines) {
        val matchResult = regex.find(line)!!
        val direction = matchResult.groups["direction"]?.value
        var clicks = matchResult.groups["clicks"]?.value?.toInt()!!

        if (direction == "L") {
            var possible = pos
            while (clicks > possible) {
                if (!startZero) {
                    numZero += 1
                }
                pos = 0
                clicks -= possible
                possible = 100
                startZero = false
            }
            pos = (pos - clicks) % 100
            if (pos < 0) {
                pos += 100
            }

        } else if (direction == "R") {
            var possible = 100 - pos
            while (clicks > possible) {
                numZero += 1
                pos = (pos + possible) % 100
                clicks -= possible
                possible = 100 - pos
            }
            pos = (pos + clicks) % 100
        }

        if (pos == 0) {
            numZero += 1
        }
        startZero = pos == 0
    }
    return numZero
}



