package pw.binom.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Property(
    val type: Type? = null,
    val format: StdFormat? = null,
    val description: String? = null,
    val items: Property? = null,
    @SerialName("\$ref")
    val ref: String? = null,
    val enum: List<String>? = null,
    val example: String? = null,
) {
    companion object {
        fun Element(type: Type, description: String?, format: StdFormat?, example: String?) =
            Property(type = type, description = description, format = format, example = example)

        fun Array(type: Type, description: String?, items: Property?) =
            Property(
                type = type,
                description = description,
                items = items,
            )

        fun Reference(ref: String, description: String?) = Property(
            ref = ref,
            description = description,
        )

        fun Enum(description: String?, enum: List<String>, example: String?) = Property(
            description = description,
            type = Type.STRING,
            enum = enum,
            format = null,
            example = example,
        )
    }
}
