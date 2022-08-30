package pw.binom.openapi

import kotlinx.serialization.descriptors.SerialDescriptor

internal inline fun <reified T : Any> SerialDescriptor.getElementAnnotation(index: Int) =
    getElementAnnotations(index).find { it is T }?.let { it as T }

internal inline fun <reified T : Any> SerialDescriptor.getAnnotation() =
    annotations.find { it is T }?.let { it as T }
