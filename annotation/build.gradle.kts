plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlinx-serialization")
    id("maven-publish")
}
kotlin {
    jvm()
    iosArm32()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    watchosArm32()
    watchosArm32()
    watchosArm64()
    watchosX86()
    watchosX64()
    watchosSimulatorArm64()
    tvosArm64()
    tvosX64()
    tvosSimulatorArm64()
    linuxX64()
    mingwX86()
    mingwX64()
    macosX64()
    macosArm64()
    linuxArm64()
    linuxArm32Hfp()
    js("js", BOTH) {
        browser()
        nodejs()
    }
    targets.all {
        compilations.findByName("main")?.compileKotlinTask?.kotlinOptions?.freeCompilerArgs =
            listOf("-opt-in=kotlin.RequiresOptIn")
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:${pw.binom.Versions.KOTLINX_SERIALIZATION_VERSION}")
            }
        }
    }
}
apply<pw.binom.publish.plugins.PrepareProject>()
