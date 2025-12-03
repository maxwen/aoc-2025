import java.io.File

fun main() {
    day2_part1()
    day2_part1Mod()
    day2_part2()
}

fun isInvalidIdPart1(id: String): Boolean {
    var subLength = 1

    // only even length can match substrings exactly twice
    if (id.length % 2 != 0) {
        return false
    }

    // makes no sense to search for longer substring
    while (subLength <= id.length / 2) {
        // not optimal to try all the lengths
        // SEE isInvalidIdPart1Mod
        val subString = id.substring(0, subLength)
        val matchString = id.substring(subLength, subLength + subLength)
        // one invalid match is enough to tell that it is valid
        if (subString.length + matchString.length == id.length && subString != matchString) {
            return false
        }
        subLength += 1
    }
    // match and length is the whole string / 2
    return true
}

fun isInvalidIdPart1Mod(id: String): Boolean {
    // only even length can match substrings exactly twice
    if (id.length % 2 != 0) {
        return false
    }

    val subLength = id.length / 2
    val subString = id.substring(0, subLength)
    val matchString = id.substring(subLength, subLength + subLength)
    return subString == matchString
}


fun isInvalidIdPart2(id: String): Boolean {
    var subLength = 1

    // still makes no sense to try longer substrings
    while (subLength <= id.length / 2) {
        val subString = id.substring(0, subLength)
        var subNum = 1
        var noMatch = false
        // check for repeating substring
        while (subLength * subNum + subLength <= id.length) {
            val matchString = id.substring(subLength * subNum, subLength * subNum + subLength)
            if (matchString != subString) {
                // if current length substrings do not match we still need to try longer substrings
                noMatch = true
                break
            }
            subNum += 1
        }

        if (!noMatch && subLength * subNum == id.length) {
            // we reached end with always matching substrings repeated
            return true
        }
        subLength += 1
    }
    return false
}

fun day2_part1() {
    val lines = File(System.getProperty("user.dir"), "input/input_day2.txt").readLines()

    var sum = 0L
    for (line in lines) {
        val sequence = line.split(",")
        for (s in sequence) {
            val (lower, upper) = s.split("-")
            val upperInt = upper.toLong()
            val lowerInt = lower.toLong()

            for (i in upperInt downTo lowerInt step 1) {

                if (isInvalidIdPart1(i.toString())) {
                    sum += i
                }
            }
        }
    }
    println(sum)
}

fun day2_part1Mod() {
    val lines = File(System.getProperty("user.dir"), "input/input_day2.txt").readLines()

    var sum = 0L
    for (line in lines) {
        val sequence = line.split(",")
        for (s in sequence) {
            val (lower, upper) = s.split("-")
            val upperInt = upper.toLong()
            val lowerInt = lower.toLong()

            for (i in upperInt downTo lowerInt step 1) {

                if (isInvalidIdPart1Mod(i.toString())) {
                    sum += i
                }
            }
        }
    }
    println(sum)
}

fun day2_part2() {
    val lines = File(System.getProperty("user.dir"), "input/input_day2.txt").readLines()

    var sum = 0L
    for (line in lines) {
        val sequence = line.split(",")
        for (s in sequence) {
            val (lower, upper) = s.split("-")
            val upperInt = upper.toLong()
            val lowerInt = lower.toLong()

            for (i in upperInt downTo lowerInt step 1) {
                if (isInvalidIdPart2(i.toString())) {
                    sum += i
                }
            }
        }
    }
    println(sum)
}



