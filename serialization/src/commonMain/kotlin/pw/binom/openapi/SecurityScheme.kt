package pw.binom.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SecurityScheme(
    @SerialName("type")
    val type: SecurityType,
    @SerialName("scheme")
    val scheme: SecurityScheme2? = null,
    @SerialName("in")
    val source: String? = null,
    val name: String? = null,
    val openIdConnectUrl: String? = null,
    val flows: Flows? = null,
)
