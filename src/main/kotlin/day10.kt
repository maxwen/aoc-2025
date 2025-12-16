import java.io.File
import java.util.BitSet
import kotlin.streams.toList

fun main() {
    println(day10_part1(File(System.getProperty("user.dir"), "input/input_day10.txt").readLines()))
    println(day10_part2(File(System.getProperty("user.dir"), "input/input_day10_test.txt").readLines()))
}

private fun applyButtons(lights: BitSet, buttons: BitSet) {
    lights.xor(buttons)
}

private fun applyButtonsJoltage(joltage: IntArray, buttons: BitSet) {
    for (i in 0..<joltage.size) {
        joltage[i] += if (buttons[i]) {
            1
        } else {
            0
        }
    }
}

private fun checkVoltageLimits(joltage: IntArray, target_joltage: IntArray): Boolean {
    // check if we are already above any target joltage limit
    for (i in 0..<joltage.size) {
        if (joltage[i] > target_joltage[i]) {
            return false
        }
    }
    return true
}

private var minButtons = Int.MAX_VALUE
private val minButtonPath = mutableListOf<BitSet>()

private fun solve(
    lights: BitSet,
    buttons: List<BitSet>,
    target_lights: BitSet,
    button_list: MutableList<BitSet>,
    button: BitSet?,
    limit: Int
) {
    val buttonCount = button_list.size
    // TODO randomly chosen upper cut to prevent endless loops
    if (buttonCount > minButtons || buttonCount > limit) {
        return
    }

    if (lights == target_lights) {
        if (buttonCount < minButtons) {
            minButtons = buttonCount
            minButtonPath.clear()
            minButtonPath.addAll(button_list)
        }
        return
    }

    for (b in buttons) {
        // makes no sense to apply the same again right away cause it just flip back to where we where before
        if (button != null && b == button) {
            continue
        }
        val l = BitSet()
        l.or(lights)

        applyButtons(lights, b)
        button_list.add(b)

        solve(lights, buttons, target_lights, button_list, b, limit)

        button_list.remove(b)

        lights.clear()
        lights.or(l)
    }
}

private var buttonCount = 0

private fun solveJoltage(
    joltage: IntArray,
    buttons: List<BitSet>,
    target_joltage: IntArray,
    button_list: MutableList<BitSet>,
    limit: Int
) {
    val buttonCount = button_list.size
    // TODO randomly chosen upper cut to prevent endless loops
    if (buttonCount > minButtons || buttonCount > limit) {
        return
    }

    // check if we are already above any target joltage limit
    if (!checkVoltageLimits(joltage, target_joltage)) {
        return
    }

    if (joltage.contentEquals(target_joltage)) {
        if (buttonCount < minButtons) {
            minButtons = buttonCount
            minButtonPath.clear()
            minButtonPath.addAll(button_list)
        }
        return
    }

    for (b in buttons) {
        val j = joltage.copyOf()
        applyButtonsJoltage(joltage, b)
        button_list.add(b)

        solveJoltage(joltage, buttons, target_joltage, button_list, limit)

        button_list.remove(b)

        for (i in 0..<j.size) {
            joltage[i] = j[i]
        }
    }
}


// 46s
fun day10_part1(lines: List<String>): Long {
    var sum = 0L
    for (line in lines) {
        if (line.isEmpty()) {
            continue
        }
        val lineParts = line.split(" ")
        val lightsPart = lineParts[0].substring(1..lineParts[0].length - 2)
        val targetLightsList = lightsPart.chars().map { c ->
            if (c.toChar() == '#') {
                1
            } else {
                0
            }
        }.toList()
        val targetLights = BitSet(targetLightsList.size)
        for (i in 0..<targetLightsList.size) {
            targetLights[i] = targetLightsList[i] == 1
        }

        val buttonsList = mutableListOf<BitSet>()

        for (x in 1..<lineParts.size) {
            val part = lineParts[x]
            if (part.startsWith("(")) {
                val buttonPart = part.substring(1..part.length - 2)
                val buttonsPartList = buttonPart.split(",").map { c ->
                    c.toInt()
                }

                val button = BitSet(buttonsPartList.size)
                for (b in buttonsPartList) {
                    button[b] = true
                }
                buttonsList.add(button)
            } else if (part.startsWith("{")) {
                // ignore
            }
        }
//        println("" + targetLights + " " + buttonsList + " " + joltageList)

        // just choose a random cut off limit
        var limit = buttonsList.size
        var solved = false

        while (!solved) {
            minButtons = Int.MAX_VALUE
            minButtonPath.clear()

            val currentLights = BitSet(targetLights.size())

            solve(currentLights, buttonsList, targetLights, mutableListOf<BitSet>(), null, limit)

            // did not got a solution
            if (minButtons == Int.MAX_VALUE) {
                limit *= 2
                continue
            }
            solved = true
//            println(minButtons)
//            println("" + minButtonPath)
            sum += minButtons
        }
    }
    return sum
}

fun day10_part2(lines: List<String>): Long {
    var sum = 0L
    for (line in lines) {
        if (line.isEmpty()) {
            continue
        }
        val lineParts = line.split(" ")

        val buttonsList = mutableListOf<BitSet>()
        var targetJoltage = listOf<Int>()

        for (x in 1..<lineParts.size) {
            val part = lineParts[x]
            if (part.startsWith("(")) {
                val buttonPart = part.substring(1..part.length - 2)
                val buttonsPartList = buttonPart.split(",").map { c ->
                    c.toInt()
                }

                val button = BitSet(buttonsPartList.size)
                for (b in buttonsPartList) {
                    button[b] = true
                }
                buttonsList.add(button)
            } else if (part.startsWith("{")) {
                val joltagePart = part.substring(1..part.length - 2)
                targetJoltage = joltagePart.split(",").map { c ->
                    c.toInt()
                }
            }
        }
        println("" + buttonsList + " " + targetJoltage)

        // just choose a random cut off limit
        var limit = buttonsList.size
        var solved = false
        val target_joltage = targetJoltage.toIntArray()

        while (!solved) {
            minButtons = Int.MAX_VALUE
            minButtonPath.clear()

            val joltage = IntArray(targetJoltage.size)

            solveJoltage(joltage, buttonsList, target_joltage, mutableListOf<BitSet>(), limit)

            // did not got a solution
            if (minButtons == Int.MAX_VALUE) {
                limit *= 2
                continue
            }
            solved = true
            println(minButtons)
//            println("" + minButtonPath)
            sum += minButtons
        }
    }
    return sum
}