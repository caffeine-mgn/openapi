package pw.binom.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PathParam(
    val name: String,
    @SerialName("in")
    val source: String,
    val required: Boolean,
    val description: String?,
    val schema: Schema3,
    val example: String?,
)
