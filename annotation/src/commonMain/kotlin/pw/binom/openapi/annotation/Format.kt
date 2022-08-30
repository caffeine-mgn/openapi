package pw.binom.openapi.annotation

import kotlinx.serialization.SerialInfo
import pw.binom.openapi.StdFormat

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Format(val value: StdFormat)
