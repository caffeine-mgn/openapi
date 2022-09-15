package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class OpenApiFile(
    val openapi: String,
    val info: Info?,
    val servers: List<Server>?,
    val tags: List<Tag>?,
    val components: Components?,
    val paths: Map<String, Path>? = null,
    val security: List<Map<String, List<String>>>? = null
)
