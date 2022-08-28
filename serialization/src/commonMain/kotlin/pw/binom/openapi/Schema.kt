package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
class Schema(
    val description: String? = null,
    val type: Type? = null,
    val properties: Map<String, Property>? = null,
    val required: List<String>? = null,
)
