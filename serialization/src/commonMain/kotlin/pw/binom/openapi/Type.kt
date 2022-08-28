package pw.binom.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Type {
    @SerialName("array")
    ARRAY,

    @SerialName("enum")
    ENUM,

    @SerialName("string")
    STRING,

    @SerialName("integer")
    INTEGER,

    @SerialName("boolean")
    BOOLEAN,

    @SerialName("number")
    NUMBER,

    @SerialName("object")
    OBJECT,
}
