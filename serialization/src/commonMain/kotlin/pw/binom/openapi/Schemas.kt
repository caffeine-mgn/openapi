package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Schemas(
    val schemas: Map<String, Schema>,
)
