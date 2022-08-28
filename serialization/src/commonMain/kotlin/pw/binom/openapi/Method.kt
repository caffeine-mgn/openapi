package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Method(
    val summary: String?,
    val description: String?,
    val parameters: List<PathParam>?,
    val requestBody: RequestBody?,
    val responses: Map<String, RequestBody>,
    val tags: List<String>?,
)
