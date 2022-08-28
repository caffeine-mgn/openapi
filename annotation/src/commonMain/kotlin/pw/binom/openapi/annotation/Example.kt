package pw.binom.openapi.annotation

import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Example(val value: String)
