import dev.holdbetter.presenter.limits.LimitsDistributor
import dev.holdbetter.presenter.limits.DayInfo
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

class LimitDistributorTest {

    // TODO: should be mocked as no-effect for tests
    val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val tomorrow = Clock.System.now().plus(1.days).toLocalDateTime(TimeZone.UTC).date
    val instant = Clock.System.now()

    @Test
    fun `returns dayRates that sum of requests not exceeded limit`() {
        val day1 = DayInfo(today, 100, 90.minutes, instant)
        val day2 = DayInfo(tomorrow, 10, 90.minutes, instant)
        val requestedRate = 240..600

        val rates = LimitsDistributor(
            mapOf(1 to day1, 2 to day2),
            requestedRate
        ).distribute(100)

        assertTrue { rates.values.sumOf { it.plannedRequestCount } <= 100 }
    }

    @Test
    fun `returns dayRates where day with most matches but same duration receives more requests`() {
        val dayMoreMatches = DayInfo(today, 12, 90.minutes, instant)
        val dayLessMatches = DayInfo(tomorrow, 10, 90.minutes, instant)

        val rates = LimitsDistributor(
            mapOf(1 to dayMoreMatches, 2 to dayLessMatches),
            1..Int.MAX_VALUE
        ).distribute(100)

        assertTrue { rates[1]!!.plannedRequestCount > rates[2]!!.plannedRequestCount }
    }

    @Test
    fun `returns dayRates where day with most duration but same count receives more requests`() {
        val dayMoreMatches = DayInfo(today, 10, 180.minutes, instant)
        val dayLessMatches = DayInfo(tomorrow, 10, 90.minutes, instant)

        val rates = LimitsDistributor(
            mapOf(1 to dayMoreMatches, 2 to dayLessMatches),
            1..Int.MAX_VALUE
        ).distribute(100)

        assertTrue { rates[1]!!.plannedRequestCount > rates[2]!!.plannedRequestCount }
    }

    @Test
    fun `returns dayRates where all days get limited rates when calculated rate is too frequent`() {
        val day1 = DayInfo(today, 10, 180.minutes, instant)
        val day2 = DayInfo(tomorrow, 10, 90.minutes, instant)
        val requestedRate = 240..600

        val rates = LimitsDistributor(
            mapOf(1 to day1, 2 to day2),
            requestedRate
        ).distribute(600)

        val minDay1 = day1.duration.inWholeSeconds / 240
        val minDay2 = day2.duration.inWholeSeconds / 240
        assertEquals(minDay1.toInt(), rates[1]!!.plannedRequestCount)
        assertEquals(minDay2.toInt(), rates[2]!!.plannedRequestCount)
    }

    @Test
    fun `returns dayRates where day 1 gets upperlimit rate when calculated rate is too rare`() {
        val day1 = DayInfo(today, 100, 900.minutes, instant)
        val day2 = DayInfo(tomorrow, 10, 90.minutes, instant)
        val requestedRate = 1..600

        val rates = LimitsDistributor(
            mapOf(1 to day1, 2 to day2),
            requestedRate
        ).distribute(100)

        val maxDay2 = day2.duration.inWholeSeconds / 600
        assertEquals(maxDay2.toInt(), rates[2]!!.plannedRequestCount)
    }

    @Test
    fun `returns dayRates with rates in requested range`() {
        val day1 = DayInfo(today, 10, 180.minutes, instant)
        val day2 = DayInfo(tomorrow, 10, 90.minutes, instant)
        val requestedRate = 100..600

        val rates = LimitsDistributor(
            mapOf(1 to day1, 2 to day2),
            requestedRate
        ).distribute(100)

        assertTrue {
            rates.all {
                it.value.rateInSeconds >= requestedRate.first &&
                        it.value.rateInSeconds <= requestedRate.last
            }
        }
    }

    @Test
    fun `throws if rate range condition impossible to met`() {
        val day1 = DayInfo(today, 1, 90.minutes, instant)
        val day2 = DayInfo(tomorrow, 1, 90.minutes, instant)
        val impossibleConditionToMet = 1..10

        assertThrows<IllegalArgumentException> {
            LimitsDistributor(
                dateAndDayInfo = mapOf(1 to day1, 2 to day2),
                rateRangeInSecondsForMatchdays = impossibleConditionToMet
            ).distribute(100)
        }
    }

    @Test
    fun `returns all rates in range`() {
        val day1 = DayInfo(today, 16, 180.minutes, instant)
        val day2 = DayInfo(tomorrow, 12, 90.minutes, instant)
        val requestedRate = 120..300

        val rates = LimitsDistributor(
            mapOf(1 to day1, 2 to day2),
            1..Int.MAX_VALUE
        ).distribute(100)

        assertTrue {
            rates.values.all {
                it.rateInSeconds in requestedRate
            }
        }
    }

    @RepeatedTest(999)
    fun `checks random days composition distribution`(info: RepetitionInfo) {
        // Arrange
        val limit = 500
        val durations = listOf(120, 180, 240, 360, 420, 600)
        val ranges = listOf(
            60..900,
            60..700,
            60..600,
            120..700,
            120..600,
            600..800,
            660..900,
            720..1000,
            720..1200,
            720..1500
        )
        val dateAndMatches = generateSequence(1) { it + 1 }
            .take(12)
            .map {
                it to DayInfo(
                    today,
                    Random.nextInt(1, 6),
                    durations[Random.nextInt(durations.lastIndex)].minutes,
                    instant
                )
            }.toMap()
        val requestedRateRange = ranges[info.currentRepetition / 100]

        // Act
        val rates = LimitsDistributor(dateAndMatches, requestedRateRange).distribute(limit)

        // Assert
        assertTrue { rates.values.sumOf { it.plannedRequestCount } <= limit }

        assertTrue {
            rates.values.all {
                it.rateInSeconds in requestedRateRange
            }
        }
    }

    @RepeatedTest(999)
    fun `returns dayRates where sum of requests is equal to limit when rate allows`() {
        val limit = 500
        val durations = listOf(120, 180, 240, 360, 420, 600)
        val requestedRate = 60..1200
        val dateAndMatches = generateSequence(1) { it + 1 }
            .take(12)
            .map {
                it to DayInfo(
                    today,
                    Random.nextInt(1, 6),
                    durations[Random.nextInt(durations.lastIndex)].minutes,
                    instant
                )
            }.toMap()

        val rates = LimitsDistributor(
            dateAndMatches,
            requestedRate
        ).distribute(limit)

        assertTrue { rates.values.sumOf { it.plannedRequestCount } == limit }
    }
}

