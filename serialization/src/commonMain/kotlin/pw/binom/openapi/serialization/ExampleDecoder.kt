package pw.binom.openapi.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.modules.SerializersModule
import pw.binom.openapi.annotation.Example
import pw.binom.openapi.annotation.GenerateExample

class ExampleDecoder<T>(
    override val serializersModule: SerializersModule,
    private val descriptor: DeserializationStrategy<T>,
    private var example: String?
) : AbstractDecoder() {
    private var cursor = -1
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        do {
            cursor++
            if (cursor >= descriptor.elementsCount) {
                return DECODE_DONE
            }
            example = descriptor.getElementAnnotations(cursor)
                .asSequence()
                .mapNotNull { it as? Example }
                .firstOrNull()?.value
            val optional = descriptor.isElementOptional(cursor) &&
                !descriptor.getElementAnnotations(cursor).any { it is GenerateExample } &&
                example == null
            if (optional) {
                continue
            }
            return cursor
        } while (true)
    }

    override fun decodeString(): String {
        example?.let { return it }
        return "string-${descriptor.descriptor.serialName}"
    }

    override fun decodeNull(): Nothing? {
        return super.decodeNull()
    }

    @ExperimentalSerializationApi
    override fun <T : Any> decodeNullableSerializableValue(deserializer: DeserializationStrategy<T?>): T? {
        return super.decodeNullableSerializableValue(deserializer)
    }

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, previousValue: T?): T {
        if (deserializer.descriptor.isNullable && example == null && previousValue != null) {
            return previousValue
        }
        val example = descriptor.descriptor.getElementAnnotations(cursor).asSequence().mapNotNull { it as? Example }
            .firstOrNull()?.value
        return deserializer.deserialize(ExampleDecoder(serializersModule, descriptor = deserializer, example = example))
    }

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        return super.decodeSerializableElement(descriptor, index, deserializer, previousValue)
    }
}
