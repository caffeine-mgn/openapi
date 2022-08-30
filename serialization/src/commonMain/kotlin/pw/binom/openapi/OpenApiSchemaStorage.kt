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
        Schemas(internalSchemaNames)

    fun getProperty(
        description: SerialDescriptor,
        hint: String?,
        example: String?,
        format: StdFormat?
    ): Property {
        val realDescription = if (description.kind is SerialKind.CONTEXTUAL) {
            serializersModule.getContextualDescriptor(description)
                ?: throw SerializationException("Can't find serializer for ${description.serialName}")
        } else {
            description
        }
        val example = example ?: description.annotations.find { it is Example }?.let { it as Example }?.value
        val format = format ?: description.getAnnotation<Format>()?.value
        return when (realDescription.kind) {
            PrimitiveKind.STRING -> Property.Element(
                format = format,
                type = Type.STRING,
                description = hint,
                example = example,
            )

            PrimitiveKind.BOOLEAN -> Property.Element(
                format = format,
                type = Type.BOOLEAN,
                description = hint,
                example = example,
            )

            PrimitiveKind.INT -> Property.Element(
                format = format ?: StdFormat.INT32,
                type = Type.INTEGER,
                description = hint,
                example = example,
            )

            PrimitiveKind.LONG -> Property.Element(
                format = format ?: StdFormat.INT64,
                type = Type.INTEGER,
                description = hint,
                example = example,
            )

            PrimitiveKind.BYTE -> Property.Element(
                format = StdFormat.INT8,
                type = Type.INTEGER,
                description = hint,
                example = example,
            )

            PrimitiveKind.SHORT -> Property.Element(
                format = format ?: StdFormat.INT16,
                type = Type.INTEGER,
                description = hint,
                example = example,
            )

            PrimitiveKind.FLOAT -> Property.Element(
                format = format ?: StdFormat.FLOAT,
                type = Type.NUMBER,
                description = hint,
                example = example,
            )

            PrimitiveKind.DOUBLE -> Property.Element(
                format = format ?: StdFormat.DOUBLE,
                type = Type.NUMBER,
                description = hint,
                example = example,
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
                    items = getProperty(description = d, hint = null, example = null, format = null),
                    type = Type.ARRAY,
                    description = hint,
                )
            }

            is StructureKind.OBJECT, is StructureKind.CLASS -> {
                if (realDescription == Unit.serializer().descriptor) {
                    Property(
                        description = hint,
                        type = Type.OBJECT,
                    )
                } else {
                    getOrCreateSchema(realDescription)
                    Property.Reference(
                        description = hint,
                        ref = "#/components/schemas/${realDescription.serialName.removeSuffix("?")}"
                    )
                }
            }

            else -> TODO("->${realDescription.kind}")
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
        if (lastDuplicateName != null) {
            val id = lastDuplicateName + 1
            schemaName = "$schemaName$id"
            schemaDuplicateNames[schemaName] = id
        } else {
            val oldDescriptionWithSameName = internalSchemaNames[schemaName]
            if (oldDescriptionWithSameName != null) {
                internalSchemaNames.remove(schemaName)
                internalSchemaNames["${schemaName}1"] = oldDescriptionWithSameName
                internalSchemaNames["${schemaName}2"] = schema
                schemaDuplicateNames[schemaName] = 2
                schemaName = "${schemaName}2"
            }
        }

        internalSchemas[nullableDescription] = schema
        internalSchemaNames[schemaName] = schema
        repeat(description.elementsCount) { index ->
            val propertyTextDescription = description.getElementAnnotation<Description>(index)?.value
            val exampleDescription = description.getElementAnnotation<Example>(index)?.value
            val format = description.getElementAnnotation<Format>(index)?.value
            val name = description.getElementName(index)
            val propertyDescription = description.getElementDescriptor(index)
            val isRequired = !description.isElementOptional(index)
            properties[name] = getProperty(
                description = propertyDescription,
                hint = propertyTextDescription,
                example = exampleDescription,
                format = format,
            )
            if (isRequired) {
                required += name
            }
        }
        return schema
    }
}
