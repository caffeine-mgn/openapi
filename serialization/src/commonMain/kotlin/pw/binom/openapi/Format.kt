package pw.binom.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Format {
    @SerialName("int8")
    INT8,

    @SerialName("int16")
    INT16,

    @SerialName("int32")
    INT32,

    @SerialName("int64")
    INT64,

    @SerialName("float")
    FLOAT,

    @SerialName("double")
    DOUBLE,
}
