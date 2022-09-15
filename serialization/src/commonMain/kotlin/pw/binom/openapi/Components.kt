package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Components(
    val schemas: Map<String, Schema>? = null,
    val securitySchemes: Map<String, SecurityScheme>? = null
)
