package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Server(
    val url: String,
    val description: String?,
)
