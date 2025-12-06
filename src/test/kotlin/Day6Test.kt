import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class Day6Test {
    @Test
    fun day6_part1() {
        val testDataString =
            "123 328  51 64 \n" +
                    " 45 64  387 23 \n" +
                    "  6 98  215 314\n" +
                    "*   +   *   +  "
        val testData = testDataString.split("\n")

        val res = day6_part1(testData)
        assertEquals(res, 4277556)
    }

    @Test
    fun day6_part2() {
        val testDataString =
            "123 328  51 64 \n" +
                    " 45 64  387 23 \n" +
                    "  6 98  215 314\n" +
                    "*   +   *   +  "
        val testData = testDataString.split("\n")

        val res = day6_part2(testData)
        assertEquals(res, 3263827)
    }
}