package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Schema2(
    val schema: Property,
    val examples: Map<String, ExampleValue>?,
)
