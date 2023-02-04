package dev.holdbetter.common.util

import dev.holdbetter.common.LeagueDTO
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray

// TODO: Test
class StandingListUnwrapper : JsonTransformingSerializer<List<LeagueDTO.StandingDTO>>(ListSerializer(LeagueDTO.StandingDTO.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return element.jsonArray.singleOrNull() ?: element
    }
}