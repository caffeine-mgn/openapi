package pw.binom.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SecurityScheme2 {
    @SerialName("bearer")
    BEARER,
    @SerialName("basic")
    BASIC,
}
