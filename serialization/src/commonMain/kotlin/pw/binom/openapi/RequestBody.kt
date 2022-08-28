package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    val content: Map<String, Schema2?>? = null,
    val description: String? = null,
)
