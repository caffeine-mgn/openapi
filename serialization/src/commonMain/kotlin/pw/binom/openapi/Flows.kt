package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
class Flows(
    val authorizationCode: Flow? = null,
    val implicit: Flow? = null,
    val password: Flow? = null,
    val clientCredentials: Flow? = null,
)
