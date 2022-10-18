package pw.binom.openapi.annotation

import kotlinx.serialization.SerialInfo

/**
 * Allow override default value and force generates example
 */
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class GenerateExample
