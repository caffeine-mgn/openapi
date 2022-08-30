package pw.binom.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StdFormat {
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

    @SerialName("date")
    DATE,

    @SerialName("date-time")
    DATE_TIME,

    @SerialName("byte")
    BASE64,

    @SerialName("binary")
    BINARY,

    @SerialName("email")
    EMAIL,

    @SerialName("uuid")
    UUID,

    @SerialName("uri")
    URI,

    @SerialName("hostname")
    HOSTNAME,

    @SerialName("ipv4")
    IPV4,

    @SerialName("ipv6")
    IPV6,
}
