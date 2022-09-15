package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Schema(
    val description: String? = null,
    val type: Type? = null,
    val properties: Map<String, Property>? = null,
    var required: List<String>? = null,
)
