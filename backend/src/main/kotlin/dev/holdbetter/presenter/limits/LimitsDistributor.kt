package dev.holdbetter.presenter.limits

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class LimitsDistributor(
    private val dateAndDayInfo: Map<Int, DayInfo>,
    private val rateRangeInSecondsForMatchdays: IntRange
) {

    private val daysAndMinMax = dateAndDayInfo.mapValues {
        calculateMinAndMaxRequestCount(
            dayDuration = it.value.duration.inWholeSeconds,
            rate = rateRangeInSecondsForMatchdays
        )
    }

    private val weights = dateAndDayInfo.mapValues { calculateWeight(it.value) }
    private val weightsSum = weights.values.sum()

    fun distribute(limit: Int): Map<Int, DayRate> {
        return resolveMatchdays(limit)
    }

    private fun resolveMatchdays(limit: Int): Map<Int, DayRate> {
        require(daysAndMinMax.values.sumOf { it.first } <= limit) {
            "Rate condition is impossible to met!"
        }

        val daysIdeal = dateAndDayInfo.mapValues {
            calculateIdealRequestCount(
                limit = limit,
                weight = weights.getValue(it.key),
                weightsSum = weightsSum
            )
        }

        val datesAndMatchesToFillNext = dateAndDayInfo.toMutableMap()

        var distribution = fillDaysWithMinRequestCount(datesAndMatchesToFillNext, daysAndMinMax)
        val remainingLimit = limit - distribution.values.sum()

        datesAndMatchesToFillNext -= getDatesMaxRequestCountReached(distribution, daysAndMinMax)

        distribution = distributeRemainingLimit(
            limit = remainingLimit,
            datesAndMatchesToFillNext = datesAndMatchesToFillNext,
            daysIdeal = daysIdeal,
            distributionByMin = distribution
        )

        return distribution.mapValues {
            DayRate(
                it.key,
                it.value,
                dateAndDayInfo.getValue(it.key)
            )
        }
    }

    private fun distributeRemainingLimit(
        limit: Int,
        datesAndMatchesToFillNext: MutableMap<Int, DayInfo>,
        daysIdeal: Map<Int, Int>,
        distributionByMin: Map<Int, Int>
    ): Map<Int, Int> {
        var remainingLimit = limit
        var distribution = distributionByMin

        while (remainingLimit >= 1 && datesAndMatchesToFillNext.isNotEmpty()) {
            val scores = weights.mapValues { w ->
                calculateScore(
                    dayToWeight = w,
                    ideal = daysIdeal.getValue(w.key),
                    currentDistributed = distribution.getValue(w.key)
                )
            }
            val scoresSum = scores.values.sum()

            val dayRates = scores.mapValues {
                calculateRequestCountBasedByScore(
                    remainingLimit = remainingLimit,
                    dayScore = it.value,
                    scoresSum = scoresSum,
                    dayMax = daysAndMinMax.getValue(it.key).second,
                    dayCurrent = distribution.getValue(it.key)
                )
            }

            if (dayRates.all { it.value < 1 }) {
                return distributeRemainingLimitDiscretely(
                    limit = remainingLimit,
                    datesAndMatchesToFillNext = datesAndMatchesToFillNext,
                    daysIdeal = daysIdeal,
                    distributionToStart = distribution
                )
            }

            distribution = distribution.mapValues {
                val toAdd = dayRates.getValue(it.key)
                it.value + toAdd
            }.toMutableMap()

            val distributedSumInIteration = dayRates.values.sum()

            datesAndMatchesToFillNext -= getDatesMaxRequestCountReached(distribution, daysAndMinMax)

            remainingLimit -= distributedSumInIteration
        }
        return distribution
    }

    private fun distributeRemainingLimitDiscretely(
        limit: Int,
        datesAndMatchesToFillNext: MutableMap<Int, DayInfo>,
        daysIdeal: Map<Int, Int>,
        distributionToStart: Map<Int, Int>
    ): Map<Int, Int> {
        var remainingLimit = limit
        val distribution = distributionToStart.toMutableMap()

        while (remainingLimit >= 1 && datesAndMatchesToFillNext.isNotEmpty()) {
            val scores = weights.map { w ->
                w.key to calculateScore(
                    dayToWeight = w,
                    ideal = daysIdeal.getValue(w.key),
                    currentDistributed = distribution.getValue(w.key)
                )
            }.sortedBy { it.second }.reversed()

            val dayToIncrement = scores.firstNotNullOfOrNull {
                val (_, max) = daysAndMinMax.getValue(it.first)
                val current = distribution.getValue(it.first)

                if (current + 1 <= max) {
                    it.first
                } else {
                    null
                }
            }

            dayToIncrement?.let {
                distribution[dayToIncrement] = distribution.getValue(dayToIncrement) + 1

                datesAndMatchesToFillNext -= getDatesMaxRequestCountReached(
                    distribution,
                    daysAndMinMax
                )
                remainingLimit -= 1
            } ?: break
        }
        return distribution
    }

    private fun calculateRequestCountBasedByScore(
        remainingLimit: Int,
        dayScore: Double,
        scoresSum: Double,
        dayMax: Int,
        dayCurrent: Int
    ): Int {
        val availableCount = dayMax - dayCurrent
        return min(
            (remainingLimit * (dayScore / scoresSum)).toInt(),
            availableCount
        )
    }

    private fun calculateScore(
        dayToWeight: Map.Entry<Int, Double>,
        ideal: Int,
        currentDistributed: Int
    ): Double {
        val weight = dayToWeight.value
        val gapP = (ideal - currentDistributed) / (ideal * 1.0)
        val gap = max(0.0, gapP)
        val score = weight * gap
        return score
    }

    private fun fillDaysWithMinRequestCount(
        datesAndMatchesToFillNext: MutableMap<Int, DayInfo>,
        daysAndMinMax: Map<Int, Pair<Int, Int>>
    ): Map<Int, Int> = datesAndMatchesToFillNext.mapValues {
        val min = daysAndMinMax.getValue(it.key).first
        min
    }

    private fun calculateMinAndMaxRequestCount(
        dayDuration: Long,
        rate: IntRange
    ): Pair<Int, Int> {
        // less rate more requests
        val (min, max) = rate.last to rate.first
        val dayMinRequestCount = ceil(dayDuration / (min * 1.0))
        val dayMaxRequestCount = floor(dayDuration / (max * 1.0))
        return dayMinRequestCount.toInt() to dayMaxRequestCount.toInt()
    }

    private fun calculateIdealRequestCount(
        limit: Int,
        weight: Double,
        weightsSum: Double
    ): Int = (limit * (weight / weightsSum)).toInt()

    private fun calculateWeight(matchday: DayInfo): Double {
        return matchday.count.toDouble().pow(0.5) * matchday.duration.inWholeMinutes
    }

    private fun getDatesMaxRequestCountReached(
        distribution: Map<Int, Int>,
        daysAndMinMax: Map<Int, Pair<Int, Int>>
    ): Set<Int> = distribution.filter {
        val (_, max) = daysAndMinMax.getValue(it.key)
        it.value == max
    }.keys
}