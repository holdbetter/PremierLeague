package dev.holdbetter.outerApi.util

import dev.holdbetter.common.util.NullableJsonTransformingSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

// TODO: Test
class LivescoreDateConverter
    : NullableJsonTransformingSerializer<Instant?>(Instant.serializer().nullable) {

    private val year = 0 until 4
    private val month = 4 until 6
    private val day = 6 until 8
    private val hour = 8 until 10
    private val minute = 10 until 12
    private val second = 12 until 14

    public override fun transformDeserialize(element: JsonElement): JsonElement {
        val livescoreDate = element.jsonPrimitive.toString()
        return convertToInstant(livescoreDate)?.let { date ->
            JsonPrimitive(
                date.toString()
            )
        } ?: JsonNull
    }

    private fun convertToInstant(livescoreDate: String): Instant? {
        return try {
            with(livescoreDate) {
                LocalDateTime(
                    year = substring(year).toInt(),
                    monthNumber = substring(month).toInt(),
                    dayOfMonth = substring(day).toInt(),
                    hour = substring(hour).toInt(),
                    minute = substring(minute).toInt(),
                    second = substring(second).toInt()
                ).toInstant(TimeZone.UTC)
            }
        } catch (e: Exception) {
            // TODO: Provide error analytics
            null
        }
    }
}