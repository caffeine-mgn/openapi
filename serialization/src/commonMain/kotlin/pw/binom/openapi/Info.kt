package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val title: String,
    val version: String,
)
