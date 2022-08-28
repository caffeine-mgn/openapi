package pw.binom.openapi

import kotlinx.serialization.Serializable

@Serializable
data class Path(
    val summary: String?,
    val description: String?,
    val parameters: List<PathParam>?,
    val get: Method? = null,
    val put: Method? = null,
    val post: Method? = null,
    val delete: Method? = null,
    val optional: Method? = null,
    val head: Method? = null,
    val patch: Method? = null,
    val trace: Method? = null,
)
