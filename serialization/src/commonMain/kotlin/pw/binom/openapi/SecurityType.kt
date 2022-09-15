package pw.binom.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SecurityType {
    @SerialName("http")
    HTTP,
    @SerialName("apiKey")
    API_KEY,
    @SerialName("openIdConnect")
    OPEN_ID_CONNECT,
    @SerialName("oauth2")
    OAUTH2,
}
