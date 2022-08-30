package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Schema3(
    val type: Type?,
    val format: StdFormat?,
)
