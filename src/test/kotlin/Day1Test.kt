import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class Day1Test {
    @Test
    fun day1_part1() {
        val testDataString =
            "L68\n" +
                    "L30\n" +
                    "R48\n" +
                    "L5\n" +
                    "R60\n" +
                    "L55\n" +
                    "L1\n" +
                    "L99\n" +
                    "R14\n" +
                    "L82"
        val testData = testDataString.split("\n")

        val res = day1_part1(testData)
        assertEquals(res, 3)
    }

    @Test
    fun day1_part2() {
        val testDataString =
            "L68\n" +
                    "L30\n" +
                    "R48\n" +
                    "L5\n" +
                    "R60\n" +
                    "L55\n" +
                    "L1\n" +
                    "L99\n" +
                    "R14\n" +
                    "L82"
        val testData = testDataString.split("\n")

        val res = day1_part2(testData)
        assertEquals(res, 6)
    }
}