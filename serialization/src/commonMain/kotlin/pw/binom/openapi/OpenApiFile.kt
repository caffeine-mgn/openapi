package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
class OpenApiFile(
    val openapi: String,
    val info: Info?,
    val servers: List<Server>?,
    val tags: List<Tag>?,
    val components: Schemas?,
    val paths: Map<String, Path>,
)
