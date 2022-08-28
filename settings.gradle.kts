pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://repo.binom.pw")
        maven(url = "https://plugins.gradle.org/m2/")
        gradlePluginPortal()
    }
}
rootProject.name = "openapi"
include(":annatation")
include(":serialization")
