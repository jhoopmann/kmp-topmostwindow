/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.12/userguide/building_java_projects.html in the Gradle documentation.
 * This project uses @Incubating APIs which are subject to change.
 */
import buildtasks.*
val targetJvmPlatform: String = project.findProperty("targetJvmPlatform")?.toString() ?: "macos"
val supportedJavaVersion: String = if (JavaVersion.current().toString().toInt() <= "21".toInt()) {
    JavaVersion.VERSION_17.toString()
} else JavaVersion.current().toString()

group = "de.jhoopmann.topmostwindow.awt"
version = "1.2.0"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("maven-publish")
}

publishing {
    repositories {
        maven {
            name = "Github"
            url = uri("https://maven.pkg.github.com/jhoopmann/kmp-topmostwindow")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("targetJvmPlatform") {
            groupId = "de.jhoopmann.topmostwindow.awt"
            artifactId = "kmp-topmostwindow-$targetJvmPlatform"
            version = project.version.toString()

            afterEvaluate {
                artifact(tasks.getByName("${targetJvmPlatform}Jar")) {
                    classifier = null
                }
                artifact(tasks.getByName("allMetadataJar")) {
                    classifier = "metadata"
                }
            }
        }
    }
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm(targetJvmPlatform) {
        compilations.all {
            kotlinOptions.jvmTarget = supportedJavaVersion
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("reflect"))
            }
        }

        macosMain {

        }

        linuxMain {

        }

        mingwMain {

        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(supportedJavaVersion.toInt())
    }
}

tasks.register<BuildNativeLibraryTask>("buildNativeLibrary")
tasks.register<CopyNativeLibraryTask>("copyNativeLibrary")

tasks.named("${targetJvmPlatform}Jar", Jar::class.java) {
    dependsOn("buildNativeLibrary")
    dependsOn("copyNativeLibrary")

    from(project.layout.buildDirectory.dir("native")) {
        into("native") // add native lib to jar/native
    }
}
