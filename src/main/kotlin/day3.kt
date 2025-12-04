import java.io.File
import kotlin.streams.toList


fun main() {
    day3_part1()
    day3_part2()
}

fun day3_part1() {
    val lines = File(System.getProperty("user.dir"), "input/input_day3.txt").readLines()
    var sum = 0
    for (line in lines) {
        val joltageValues = line.chars().map { j -> j.toChar().digitToInt() }.toList()
        var currentValue = ""
        var currentMaxJoltage = 0
        var idx = 0
        for (j in joltageValues) {
            currentValue = j.toString()
            for (subJ in joltageValues.subList(idx + 1, joltageValues.size)) {
                currentValue += subJ.toString()
                val v = currentValue.toInt()
                if (v > currentMaxJoltage) {
                    currentMaxJoltage = v
                }
                currentValue = j.toString()
            }
            idx += 1
            if (idx == joltageValues.size - 1) {
                break
            }
        }
        sum += currentMaxJoltage
    }
    println(sum)
}

// classic slow recursive solution but we can cut a few
// corners if the result cant get larger then the max we
// currently have
fun combinations1(
    arr: List<String>,
    len: Int,
    startPosition: Int,
    result: Array<String>,
    currentMaxJoltage: Long,
): Long {
    var maxJoltage = currentMaxJoltage
    val joltage = result.joinToString("").toLong()

    if (len == 0) {
        if (joltage > currentMaxJoltage) {
            return joltage
        }
        return currentMaxJoltage
    }

    // we can skip if the result cant get larger anyway
    if (joltage < currentMaxJoltage
    ) {
        return currentMaxJoltage
    }


//    println("" + len + " " + result.joinToString("").toLong() + " " + currentMaxJoltageArray.joinToString("").toLong())

    for (i in startPosition..arr.size - len) {
        result[result.size - len] = arr[i]
        maxJoltage = combinations1(arr, len - 1, i + 1, result, maxJoltage)
    }
    return maxJoltage
}

fun day3_part2() {
    val lines = File(System.getProperty("user.dir"), "input/input_day3.txt").readLines()
    var sum = 0L
    var idx = 0
    for (line in lines) {
//        println("" + idx + ":" + line)
        val joltageValues = line.map { j -> j.toString() }
        val maxJoltage = combinations1(
            joltageValues,
            12,
            0,
            arrayOf("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"),
            0,
        )

        sum += maxJoltage
        idx += 1
    }
    println(sum)
}



