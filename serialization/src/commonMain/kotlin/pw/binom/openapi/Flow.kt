package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Flow(
    val authorizationUrl: String? = null,
    val tokenUrl: String? = null,
    val refreshUrl: String? = null,
    val security: Map<String, List<String>>? = null,
)
