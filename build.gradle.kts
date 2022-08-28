/*
plugins {
    id 'org.jetbrains.kotlin.multiplatform' version '1.6.10'
    id 'java'
}

group 'pw.binom.gateway'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.7.2'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.7.2'
}

kotlin {
    /* Targets configuration omitted.
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

    sourceSets {
        commonMain {
            dependencies {
                implementation kotlin('stdlib-common')
            }
        }
        commonTest {
            dependencies {
                implementation kotlin('test-common')
                implementation kotlin('test-annotations-common')
            }
        }
    }
}
test {
    useJUnitPlatform()
}
*/

allprojects {
    group = "pw.binom.openapi"
    version = "0.1"

    repositories {
        mavenLocal()
        maven(url = "https://repo.binom.pw")
//        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
    }
}
