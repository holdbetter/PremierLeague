package dev.holdbetter.common.util

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.json.internal.writeJson

// TODO: https://github.com/Kotlin/kotlinx.serialization/issues/1927
/**
 * Nullable representative of JsonTransformingSerializer
 * @see JsonTransformingSerializer
 */
abstract class NullableJsonTransformingSerializer<T : Any?>(
    private val tSerializer: KSerializer<T?>
) : KSerializer<T?> {

    /**
     * A descriptor for this transformation.
     * By default, it delegates to [tSerializer]'s descriptor.
     *
     * However, this descriptor can be overridden to achieve better representation of the resulting JSON shape
     * for schema generating or introspection purposes.
     */
    override val descriptor: SerialDescriptor get() = tSerializer.descriptor

    @OptIn(InternalSerializationApi::class)
    final override fun serialize(encoder: Encoder, value: T?) {
        val output = encoder.asJsonEncoder()
        var element = output.json.writeJson(value, tSerializer)
        element = transformSerialize(element)
        output.encodeJsonElement(element)
    }

    final override fun deserialize(decoder: Decoder): T? {
        val input = decoder.asJsonDecoder()
        val element = input.decodeJsonElement()
        return input.json.decodeFromJsonElement(tSerializer, transformDeserialize(element))
    }

    /**
     * Transformation that happens during [deserialize] call.
     * Does nothing by default.
     *
     * During deserialization, a value from JSON is firstly decoded to a [JsonElement],
     * user transformation in [transformDeserialize] is applied,
     * and then resulting [JsonElement] is deserialized to [T] with [tSerializer].
     */
    protected open fun transformDeserialize(element: JsonElement): JsonElement = element

    /**
     * Transformation that happens during [serialize] call.
     * Does nothing by default.
     *
     * During serialization, a value of type [T] is serialized with [tSerializer] to a [JsonElement],
     * user transformation in [transformSerialize] is applied, and then resulting [JsonElement] is encoded to a JSON string.
     */
    protected open fun transformSerialize(element: JsonElement): JsonElement = element

    private fun Encoder.asJsonEncoder() = this as? JsonEncoder ?: throw IllegalStateException(
        "This serializer can be used only with Json format." +
                "Expected Encoder to be JsonEncoder, got ${this::class}"
    )

    private fun Decoder.asJsonDecoder() = this as? JsonDecoder ?: throw IllegalStateException(
        "This serializer can be used only with Json format." +
                "Expected Decoder to be JsonDecoder, got ${this::class}"
    )
}