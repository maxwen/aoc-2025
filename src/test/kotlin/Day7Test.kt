import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class Day7Test {
    @Test
    fun day7_part1() {
        val testDataString =
            ".......S.......\n" +
                    "...............\n" +
                    ".......^.......\n" +
                    "...............\n" +
                    "......^.^......\n" +
                    "...............\n" +
                    ".....^.^.^.....\n" +
                    "...............\n" +
                    "....^.^...^....\n" +
                    "...............\n" +
                    "...^.^...^.^...\n" +
                    "...............\n" +
                    "..^...^.....^..\n" +
                    "...............\n" +
                    ".^.^.^.^.^...^.\n" +
                    "..............."
        val testData = testDataString.split("\n")

        val res = day7_part1(testData)
        assertEquals(res, 21)
    }

    @Test
    fun day7_part2() {
        val testDataString =
            ".......S.......\n" +
                    "...............\n" +
                    ".......^.......\n" +
                    "...............\n" +
                    "......^.^......\n" +
                    "...............\n" +
                    ".....^.^.^.....\n" +
                    "...............\n" +
                    "....^.^...^....\n" +
                    "...............\n" +
                    "...^.^...^.^...\n" +
                    "...............\n" +
                    "..^...^.....^..\n" +
                    "...............\n" +
                    ".^.^.^.^.^...^.\n" +
                    "..............."
        val testData = testDataString.split("\n")

        val res = day7_part2(testData)
        assertEquals(res, 40)
    }
}