package pw.binom.openapi

import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.modules.SerializersModule
import pw.binom.openapi.annotation.*

class OpenApiSchemaStorage(private val serializersModule: SerializersModule) {
    private val internalSchemas = HashMap<SerialDescriptor, Schema>()
    private val internalSchemaNames = HashMap<String, Schema>()
    private val schemaDuplicateNames = HashMap<String, Int>()

    fun buildSchemas() =
        Components(internalSchemaNames)

    fun getProperty(
        description: SerialDescriptor,
        hint: String? = null,
        example: String? = null,
        format: StdFormat? = null,
        pattern: String? = null,
        minLength: Long? = null,
        maxLength: Long? = null,
    ): Property {
        val realDescription = if (description.kind is SerialKind.CONTEXTUAL) {
            serializersModule.getContextualDescriptor(description)
                ?: throw SerializationException("Can't find serializer for ${description.serialName}")
        } else {
            description
        }
        val example = example ?: description.annotations.find { it is Example }?.let { it as Example }?.value
        val format = format ?: description.getAnnotation<Format>()?.value
        val pattern = pattern ?: description.getAnnotation<Pattern>()?.value
        val minLength = minLength ?: description.getAnnotation<MinLength>()?.value
        val maxLength = maxLength ?: description.getAnnotation<MaxLength>()?.value
        return when (realDescription.kind) {
            PrimitiveKind.STRING -> Property.Element(
                format = format,
                type = Type.STRING,
                description = hint,
                example = example,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )

            PrimitiveKind.BOOLEAN -> Property.Element(
                format = format,
                type = Type.BOOLEAN,
                description = hint,
                example = example,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )

            PrimitiveKind.INT -> Property.Element(
                format = format ?: StdFormat.INT32,
                type = Type.INTEGER,
                description = hint,
                example = example,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )

            PrimitiveKind.LONG -> Property.Element(
                format = format ?: StdFormat.INT64,
                type = Type.INTEGER,
                description = hint,
                example = example,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )

            PrimitiveKind.BYTE -> Property.Element(
                format = StdFormat.INT8,
                type = Type.INTEGER,
                description = hint,
                example = example,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )

            PrimitiveKind.SHORT -> Property.Element(
                format = format ?: StdFormat.INT16,
                type = Type.INTEGER,
                description = hint,
                example = example,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )

            PrimitiveKind.FLOAT -> Property.Element(
                format = format ?: StdFormat.FLOAT,
                type = Type.NUMBER,
                description = hint,
                example = example,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )

            PrimitiveKind.DOUBLE -> Property.Element(
                format = format ?: StdFormat.DOUBLE,
                type = Type.NUMBER,
                description = hint,
                example = example,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )

            is SerialKind.ENUM -> {
                val enums = ArrayList<String>()
                repeat(realDescription.elementsCount) { index ->
                    enums += realDescription.getElementName(index)
                }
                Property.Enum(
                    description = hint,
                    enum = enums,
                    example = example,
                )
            }

            is StructureKind.LIST -> {
                val d = realDescription.getElementDescriptor(0)
                Property.Array(
                    items = getProperty(
                        description = d,
                        hint = null,
                        example = null,
                        format = null,
                        pattern = pattern,
                        maxLength = null,
                        minLength = null
                    ),
                    type = Type.ARRAY,
                    description = hint,
                    minLength = minLength,
                    maxLength = maxLength,
                )
            }

            is StructureKind.OBJECT, is StructureKind.CLASS -> {
                if (realDescription == Unit.serializer().descriptor) {
                    Property(
                        description = hint,
                        type = Type.OBJECT,
                    )
                } else {
                    val schema = getOrCreateSchema(realDescription)
                    Property.Reference(
                        description = hint,
                        ref = "#/components/schemas/${realDescription.serialName.removeSuffix("?")}"
                    )
                }
            }

            is StructureKind.MAP -> {
                val key = realDescription.getElementDescriptor(0).nullable
                val value = realDescription.getElementDescriptor(1)

                if (key == String.serializer().descriptor.nullable) {
                    val valueProperty = getProperty(
                        description = value,
                    )
                    Property(
                        description = hint,
                        type = Type.OBJECT,
                        additionalProperties = valueProperty,
                        uniqueItems = true
                    )
                } else {
                    TODO("$key-$value")
                }
            }

            else -> TODO("->${realDescription.kind} in ${realDescription.serialName}")
        }
    }

    fun getOrCreateSchema(description: SerialDescriptor): Schema {
        val nullableDescription = description.nullable
        val existSchema = internalSchemas[nullableDescription]
        if (existSchema != null) {
            return existSchema
        }
        val type = when (description.kind) {
            is StructureKind.OBJECT, is StructureKind.CLASS -> Type.OBJECT
            else -> throw IllegalArgumentException("Can't create schema for kind ${description.kind}")
        }
        val properties = HashMap<String, Property>()
        val required = ArrayList<String>()
        val schemaDescription = (description.annotations.find { it is Description } as Description?)?.value
        val schema = Schema(type = type, properties = properties, required = required, description = schemaDescription)

        var schemaName = description.serialName.removeSuffix("?")
        val lastDuplicateName = schemaDuplicateNames[schemaName]
        println("processing $schemaName, lastDuplicateName=$lastDuplicateName")
        if (lastDuplicateName != null) {
            val id = lastDuplicateName + 1
            println("created with order $schemaName -> $schemaName$id")
            schemaName = "$schemaName$id"
            schemaDuplicateNames[schemaName] = id
        } else {
            val oldDescriptionWithSameName = internalSchemaNames[schemaName]
            if (oldDescriptionWithSameName != null) {
//                internalSchemaNames.remove(schemaName)
//                internalSchemaNames["${schemaName}1"] = oldDescriptionWithSameName
                internalSchemaNames["${schemaName}1"] = schema
                schemaDuplicateNames[schemaName] = 1
//                println("renamed $schemaName -> ${schemaName}1")
                println("created $schemaName -> ${schemaName}1")
                schemaName = "${schemaName}1"
            }
        }

        internalSchemas[nullableDescription] = schema
        internalSchemaNames[schemaName] = schema
        println("saved as $schemaName")
        repeat(description.elementsCount) { index ->
            val propertyTextDescription = description.getElementAnnotation<Description>(index)?.value
            val exampleDescription = description.getElementAnnotation<Example>(index)?.value
            val format = description.getElementAnnotation<Format>(index)?.value
            val pattern = description.getElementAnnotation<Pattern>(index)?.value
            val minLength = description.getElementAnnotation<MinLength>(index)?.value
            val maxLength = description.getElementAnnotation<MaxLength>(index)?.value
            val name = description.getElementName(index)
            val propertyDescription = description.getElementDescriptor(index)
            val isRequired = !description.isElementOptional(index)
            properties[name] = getProperty(
                description = propertyDescription,
                hint = propertyTextDescription,
                example = exampleDescription,
                format = format,
                pattern = pattern,
                minLength = minLength,
                maxLength = maxLength,
            )
            if (isRequired) {
                required += name
            }
        }
        if (schema.required?.isEmpty() == true) {
            schema.required = null
        }
        return schema
    }
}
