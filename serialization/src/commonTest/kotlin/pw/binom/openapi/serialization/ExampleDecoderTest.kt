package pw.binom.openapi.serialization

import kotlinx.serialization.Serializable
import pw.binom.openapi.annotation.Example
import pw.binom.openapi.annotation.GenerateExample
import kotlin.test.Test

class ExampleDecoderTest {
    @Serializable
    data class T1(
        @Example("t1-example")
        val name: String
    )

    @Serializable
    data class Data(
        @GenerateExample
        val t4: T1 = T1("MyValue"),
        val nullableWithoutExample: String?,
        @Example("v1")
        val nullableWithExample: String?,
        @Example("v2")
        @GenerateExample
        val nullableWithExample2: String?,
        @GenerateExample
        val nullableWithExample3: String,
        val nullableWithDefault: String? = "123",
        val nullableWithDefault2: String? = null,
        @GenerateExample
        val nullableWithDefault3: String? = null,
        @GenerateExample
        @Example("v3")
        val nullableWithDefault4: String? = null,
        @Example("v4")
        val nullableWithDefault5: String? = null,
        val t1: T1,
        val t2: T1?,
        @GenerateExample
        val t3: T1?
    )

    @Test
    fun test() {
        val example = ExampleGenerator.generate(Data.serializer())
        println("out=$example")
    }
}
