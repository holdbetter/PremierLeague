package dev.holdbetter.outerApi.util

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

// TODO: Test
internal class TeamIdFromMatchUnwrapper :
    JsonTransformingSerializer<String>(String.serializer()) {

    companion object {
        const val ID = "ID"
    }

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return element.jsonArray
            .singleOrNull()
            ?.jsonObject
            ?.get(ID) ?: element
    }
}