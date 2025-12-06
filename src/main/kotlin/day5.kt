import java.io.File

fun main() {
    day5_part1()
    day5_part2()
}

fun day5_part1() {
    val lines = File(System.getProperty("user.dir"), "input/input_day5.txt").readLines()
    val id_range_regex = Regex("(?<lower>[0-9]+)-(?<upper>[0-9]+)")

    var sum = 0
    val ranges = mutableListOf<LongRange>()
    var part2 = false
    for (line in lines) {
        if (line.isEmpty()) {
            if (part2) {
                continue
            }
            part2 = true
            continue
        }
        if (!part2) {
            val matchResult = id_range_regex.find(line)!!
            val lower = matchResult.groups["lower"]?.value?.toLong()!!
            val upper = matchResult.groups["upper"]?.value?.toLong()!!
            ranges.add(LongRange(lower, upper))
        } else {
            val id = line.toLong()
            if (ranges.any { range -> range.contains(id) }) {
                sum += 1
            }
        }
    }
    println(sum)
}

fun day5_part2() {
    val lines = File(System.getProperty("user.dir"), "input/input_day5.txt").readLines()
    val id_range_regex = Regex("(?<lower>[0-9]+)-(?<upper>[0-9]+)")

    var sum = 0L
    val ranges = mutableListOf<LongRange>()
    for (line in lines) {
        if (line.isEmpty()) {
            break
        }
        val matchResult = id_range_regex.find(line)!!
        val lower = matchResult.groups["lower"]?.value?.toLong()!!
        val upper = matchResult.groups["upper"]?.value?.toLong()!!
        ranges.add(LongRange(lower, upper))
    }

    // merge ranges - repeat until we cant merge anymore
    val mergedRanges = mutableListOf<LongRange>()
    var mergedRangesSize = 0
    while (true) {
        for (range in ranges) {
            val newMergedRanges = mutableListOf<LongRange>()
            var mergedRange = false
            if (mergedRanges.isEmpty()) {
                newMergedRanges.add(range)
            } else {
                // check if range is inside a range or extends below or above
                for (merged in mergedRanges) {
                    if (merged.contains(range.first) && merged.contains(range.last)) {
                        // fully inside
                        newMergedRanges.add(merged)
                        mergedRange = true
                    } else if (merged.contains(range.first) && !merged.contains(range.last)) {
                        // extends above
                        val newMerged = LongRange(merged.first, range.last)
                        newMergedRanges.add(newMerged)
                        mergedRange = true
                    } else if (merged.contains(range.last) && !merged.contains(range.first)) {
                        // extends below
                        val newMerged = LongRange(range.first, merged.last)
                        newMergedRanges.add(newMerged)
                        mergedRange = true
                    } else {
                        // outside
                        newMergedRanges.add(merged)
                    }
                }
                if (!mergedRange) {
                    // add as new if outside anything
                    newMergedRanges.add(range)
                }
            }
            mergedRanges.clear()
            mergedRanges.addAll(newMergedRanges)
        }

        ranges.clear()
        ranges.addAll(mergedRanges)

        // we need to repeat this until we cant merge anything anymore
        if (mergedRangesSize == ranges.size) {
            break
        }

        // next round
        mergedRangesSize = ranges.size
        mergedRanges.clear()
    }

    for (merged in mergedRanges) {
        sum += merged.last - merged.first + 1
    }
    println(sum)
}
