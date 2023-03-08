package dev.holdbetter.outerApi.util

import dev.holdbetter.common.util.NullableJsonTransformingSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

// TODO: Test
class LivescoreDateConverter
    : NullableJsonTransformingSerializer<LocalDateTime?>(LocalDateTime.serializer().nullable) {

    private val year = 0 until 4
    private val month = 4 until 6
    private val day = 6 until 8
    private val hour = 8 until 10
    private val minute = 10 until 12
    private val second = 12 until 14

    public override fun transformDeserialize(element: JsonElement): JsonElement {
        val livescoreDate = element.jsonPrimitive.toString()
        return convertToLocalDateTime(livescoreDate)?.let { date ->
            JsonPrimitive(
                date.toString()
            )
        } ?: JsonNull
    }

    private fun convertToLocalDateTime(livescoreDate: String): LocalDateTime? {
        return try {
            with(livescoreDate) {
                LocalDateTime(
                    year = substring(year).toInt(),
                    monthNumber = substring(month).toInt(),
                    dayOfMonth = substring(day).toInt(),
                    hour = substring(hour).toInt(),
                    minute = substring(minute).toInt(),
                    second = substring(second).toInt()
                )
            }
        } catch (e: Exception) {
            // TODO: Provide error analytics
            null
        }
    }
}