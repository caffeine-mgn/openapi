package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val description: String?,
)
