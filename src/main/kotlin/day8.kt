import java.io.File
import java.util.SortedMap
import kotlin.math.sqrt
import kotlin.math.pow

fun main() {
    println(day8_part1(File(System.getProperty("user.dir"), "input/input_day8.txt").readLines(), 1000))
    println(day8_part2(File(System.getProperty("user.dir"), "input/input_day8.txt").readLines()))
}

private class Point3D(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0
) {

    fun distance(other: Point3D): Double {
        return sqrt((other.x - x).pow(2) + (other.y - y).pow(2) + (other.z - z).pow(2))
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is Point3D && other.x == x && other.y == y && other.z == z

    }

    override fun toString(): String {
        return "$x,$y,$z"
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }
}

private class Circuit(var nodes: MutableSet<Point3D> = mutableSetOf()) {
    fun contains(node: Point3D): Boolean {
        return nodes.contains(node)
    }

    fun addNode(node: Point3D) {
        nodes.add(node)
    }

    override fun toString(): String {
        return "" + nodes
    }
}

private fun parseCoords(lines: List<String>): List<Point3D> {
    val coordsList = mutableListOf<Point3D>()
    for (line in lines) {
        val coordParts = line.split(",")
        val p = Point3D(
            coordParts[0].trim().toDouble(),
            coordParts[1].trim().toDouble(),
            coordParts[2].trim().toDouble()
        )
        coordsList.add(p)
    }
    return coordsList
}

private fun calcDistances(coordsList: List<Point3D>): SortedMap<Double, Pair<Point3D, Point3D>> {
    var distances = sortedMapOf<Double, Pair<Point3D, Point3D>>()
    sortedMapOf<Double, Pair<Point3D, Point3D>>()
    for (p in coordsList) {
        for (q in coordsList) {
            if (p == q) {
                continue
            }
            val d = p.distance(q)
            distances[d] = p to q
        }
    }
    return distances
}

fun day8_part1(lines: List<String>, closestSize: Int): Long {
    val coordsList = parseCoords(lines)
    var sum = 1L
    var distances = calcDistances(coordsList)

    var circuits = mutableListOf<Circuit>()
    val distanceIter = distances.values.iterator()
    outerlop@ for (i in 0..<closestSize) {
        val minDistancePair = distanceIter.next()

        for (c in circuits) {
            if (c.contains(minDistancePair.first) && c.contains(minDistancePair.second)) {
                // already direct connection
                continue@outerlop
            }
        }

        var c1: Circuit? = circuits.find { c -> c.contains(minDistancePair.first) }
        var c2: Circuit? = circuits.find { c -> c.contains(minDistancePair.second) }

        if (c1 == null && c2 != null) {
            c2.addNode(minDistancePair.first)
        }
        if (c1 != null && c2 == null) {
            c1.addNode(minDistancePair.second)
        }
        if (c1 != null && c2 != null) {
            // merge - dont care about adding multiple times - its a set anyway
            c1.nodes.addAll(c2.nodes)
            c1.addNode(minDistancePair.first)
            c1.addNode(minDistancePair.second)
            circuits.remove(c2)
        }
        if (c1 == null && c2 == null) {
            val c = Circuit()
            c.addNode(minDistancePair.first)
            c.addNode(minDistancePair.second)
            circuits.add(c)
        }
    }

    val circuitsMap = sortedMapOf<Int, MutableList<Circuit>>(Comparator.reverseOrder())
    for (c in circuits) {
        if (!circuitsMap.containsKey(c.nodes.size)) {
            circuitsMap[c.nodes.size] = mutableListOf<Circuit>()
        }
        circuitsMap[c.nodes.size]!!.add(c)
    }

    for (c in circuitsMap.keys.take(3)) {
        sum *= c
    }
    return sum
}

fun day8_part2(lines: List<String>): Long {

    val coordsList = parseCoords(lines)
    var sum = 1L
    var distances = calcDistances(coordsList)

    var circuits = mutableListOf<Circuit>()
    val distanceIter = distances.values.iterator()
    outerlop@ for (i in 0..<distances.size) {
        val minDistancePair = distanceIter.next()

        for (c in circuits) {
            if (c.contains(minDistancePair.first) && c.contains(minDistancePair.second)) {
                // already direct connection
                continue@outerlop
            }
        }

        var c1: Circuit? = circuits.find { c -> c.contains(minDistancePair.first) }
        var c2: Circuit? = circuits.find { c -> c.contains(minDistancePair.second) }

        if (c1 == null && c2 != null) {
            c2.addNode(minDistancePair.first)
        }
        if (c1 != null && c2 == null) {
            c1.addNode(minDistancePair.second)
        }
        if (c1 != null && c2 != null) {
            // merge - dont care about adding multiple times - its a set anyway
            c1.nodes.addAll(c2.nodes)
            c1.addNode(minDistancePair.first)
            c1.addNode(minDistancePair.second)
            circuits.remove(c2)
        }
        if (c1 == null && c2 == null) {
            val c = Circuit()
            c.addNode(minDistancePair.first)
            c.addNode(minDistancePair.second)
            circuits.add(c)
        }
        if (circuits.first().nodes.size == coordsList.size) {
            sum = minDistancePair.first.x.toLong() * minDistancePair.second.x.toLong()
            break
        }
    }
    return sum
}



