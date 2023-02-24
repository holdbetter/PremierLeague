package dev.holdbetter.outerApi.util

import dev.holdbetter.outerApi.model.LivescoreDataResponse
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

// TODO: Test
internal class LivescoreUnwrapper :
    JsonTransformingSerializer<LivescoreDataResponse>(LivescoreDataResponse.serializer()) {

    companion object {
        const val STAGES = "Stages"
    }

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return element.jsonObject[STAGES]
            ?.jsonArray
            ?.firstOrNull() ?: element
    }
}