package pw.binom.openapi.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

object ExampleGenerator {
    fun <T : Any> generate(
        serializer: KSerializer<T>,
        serializersModule: SerializersModule = EmptySerializersModule
    ): T = serializer.deserialize(ExampleDecoder(serializersModule, serializer, null))

    @OptIn(InternalSerializationApi::class)
    inline fun <reified T : Any> generate(serializersModule: SerializersModule = EmptySerializersModule) =
        generate(
            serializer = T::class.serializerOrNull() ?: throw SerializationException("${T::class} is not serializable"),
            serializersModule = serializersModule
        )
}
