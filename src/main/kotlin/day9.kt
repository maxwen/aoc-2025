import java.io.File
import java.util.SortedMap
import java.util.SortedSet
import kotlin.collections.sortedMapOf
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.math.pow

fun main() {
    println(day9_part1(File(System.getProperty("user.dir"), "input/input_day9.txt").readLines()))
    println(day9_part2(File(System.getProperty("user.dir"), "input/input_day9.txt").readLines()))
}

private var maxX = 0
private var maxY = 0
private var minX = Int.MAX_VALUE
private var minY = Int.MAX_VALUE

private fun parseCoords(lines: List<String>): List<Pair<Int, Int>> {
    val coordsList = mutableListOf<Pair<Int, Int>>()
    for (line in lines) {
        val coordParts = line.split(",")
        val x = coordParts[0].trim().toInt()
        val y = coordParts[1].trim().toInt()
        coordsList.add(x to y)
        maxX = max(maxX, x)
        maxY = max(maxY, y)
        minX = min(minX, x)
        minY = min(minY, y)
    }
    return coordsList
}

private fun print_map(borderCoords: Set<Pair<Int, Int>>, innerCoords: Set<Pair<Int, Int>>) {
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            var pos = x to y
            if (pos in borderCoords) {
                print("X")
            } else if (pos in innerCoords) {
                print("O")
            } else {
                print(".")
            }
            if (x == maxX) {
                println()
            }
        }
    }
}

private fun calcRectSize(corner1: Pair<Int, Int>, corner2: Pair<Int, Int>): Long {
    val width = abs(corner1.first - corner2.first) + 1
    val height = abs(corner1.second - corner2.second) + 1
    return width.toLong() * height.toLong()
}

private fun buildRectList(coordsList: List<Pair<Int, Int>>) : SortedMap<Long, Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val rectList = sortedMapOf<Long, Pair<Pair<Int, Int>, Pair<Int, Int>>>(Comparator.reverseOrder())
    for (c1 in coordsList) {
        for (c2 in coordsList) {
            if (c1 != c2) {
                val minX = if (c1.first <= c2.first) {
                    c1.first
                } else {
                    c2.first
                }
                val maxX = if (c1.first >= c2.first) {
                    c1.first
                } else {
                    c2.first
                }
                val minY = if (c1.second <= c2.second) {
                    c1.second
                } else {
                    c2.second
                }
                val maxY = if (c1.second >= c2.second) {
                    c1.second
                } else {
                    c2.second
                }
                val topLeft = minX to minY
                val bottomRight = maxX to maxY
                val size = calcRectSize(topLeft, bottomRight)
                rectList[size] = topLeft to bottomRight
            }
        }
    }
    return rectList
}

fun day9_part1(lines: List<String>): Long {
    maxX = 0
    maxY = 0
    val coordsList = parseCoords(lines)
    val rectList = buildRectList(coordsList)
    return rectList.firstKey()
}

// https://www.algorithms-and-technologies.com/point_in_polygon/java
private fun pointInPolygon(polygon: List<Pair<Int, Int>>, point: Pair<Int, Int>): Boolean {
    //A point is in a polygon if a line from the point to infinity crosses the polygon an odd number of times
    var odd = false
    // int totalCrosses = 0; // this is just used for debugging
    //For each edge (In this case for each point of the polygon and the previous one)
    var i = 0
    var j = polygon.size - 1
    while (i < polygon.size) {
        // Starting with the edge from the last to the first node
        //If a line from the point into infinity crosses this edge
        if (((polygon[i].second > point.second) != (polygon[j].second > point.second)) // One point needs to be above, one below our y coordinate
            // ...and the edge doesn't cross our Y corrdinate before our x coordinate (but between our x coordinate and infinity)
            && (point.first < (polygon[j].first - polygon[i].first) * (point.second - polygon[i].second) / (polygon[j].second - polygon[i].second) + polygon[i].first)
        ) {
            // Invert odd
            // System.out.println("Point crosses edge " + (j + 1));
            // totalCrosses++;
            odd = !odd
        }
        //else {System.out.println("Point does not cross edge " + (j + 1));}
        j = i
        i++
    }
    // System.out.println("Total number of crossings: " + totalCrosses);
    //If the number of crossings was odd, the point is in the polygon
    return odd
}

fun day9_part2(lines: List<String>): Long {
    maxX = 0
    maxY = 0
    val coordsList = parseCoords(lines)
    val rectList = buildRectList(coordsList)

    val borderCoords = mutableSetOf<Pair<Int, Int>>()
    borderCoords.addAll(coordsList)

    for (i in 0..<coordsList.size) {
        val p = coordsList[i]
        val next = coordsList[(i + 1) % coordsList.size]
        if (p.first == next.first) {
            // vertical
            for (y in min(p.second, next.second) + 1..<max(p.second, next.second)) {
                borderCoords.add(p.first to y)
            }
        }
        if (p.second == next.second) {
            // horizontal
            for (x in min(p.first, next.first) + 1..<max(p.first, next.first)) {
                borderCoords.add(x to p.second)
            }
        }
    }

    // slow as fu.. but it works
    val pointInPolyCache = mutableMapOf<Pair<Int, Int>, Boolean>()
    var maxSize = 0L
    var i = 0
    outerloop@for (r in rectList) {
        val size = r.key
        println(i)
        i++

        if (size > maxSize) {
            val topLeft = r.value.first
            val bottomRight = r.value.second
            // check if all rect edges are either border or pointInPoly
            for (x in topLeft.first..bottomRight.first) {
                val edgeTop = x to topLeft.second
                if (edgeTop !in borderCoords) {
                    if (!pointInPolyCache.containsKey(edgeTop)) {
                        val inPoly = pointInPolygon(coordsList, edgeTop)
                        pointInPolyCache[edgeTop] = inPoly
                    }
                    if (!pointInPolyCache[edgeTop]!!) {
                        continue@outerloop
                    }
                }

                val edgeBottom = x to bottomRight.second
                if (edgeBottom !in borderCoords) {
                    if (!pointInPolyCache.containsKey(edgeBottom)) {
                        val inPoly = pointInPolygon(coordsList, edgeBottom)
                        pointInPolyCache[edgeBottom] = inPoly
                    }
                    if (!pointInPolyCache[edgeBottom]!!) {
                        continue@outerloop
                    }
                }
            }
            for (y in topLeft.second..bottomRight.second) {
                val leftEdge = topLeft.first to y
                if (leftEdge !in borderCoords) {
                    if (!pointInPolyCache.containsKey(leftEdge)) {
                        val inPoly = pointInPolygon(coordsList, leftEdge)
                        pointInPolyCache[leftEdge] = inPoly
                    }
                    if (!pointInPolyCache[leftEdge]!!) {
                        continue@outerloop
                    }
                }

                val rightEdge = bottomRight.first to y
                if (rightEdge !in borderCoords) {
                    if (!pointInPolyCache.containsKey(rightEdge)) {
                        val inPoly = pointInPolygon(coordsList, rightEdge)
                        pointInPolyCache[rightEdge] = inPoly
                    }
                    if (!pointInPolyCache[rightEdge]!!) {
                        continue@outerloop
                    }
                }
            }
            maxSize = size
        }
    }

    return maxSize
}



