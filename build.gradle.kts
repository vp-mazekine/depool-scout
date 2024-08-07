import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.mazekine.depool.scout"
version = "1.0-SNAPSHOT"

val log4jVersion = "2.23.1"
val ktorClientVersion = "2.3.12"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("tech.deplant.java4ever:java4ever-framework:3.1.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.1")

    //  Logging
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.4.0")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.8.1")

    //  HTTP
    implementation("io.ktor:ktor-client-core:$ktorClientVersion")
    implementation("io.ktor:ktor-client-websockets:$ktorClientVersion")
    implementation("io.ktor:ktor-client-cio:$ktorClientVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorClientVersion")
    implementation("io.ktor:ktor-serialization-gson:$ktorClientVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(22)
}

application {
    mainClass.set("com.mazekine.depool.scout.MainKt")
    applicationDefaultJvmArgs = listOf("--enable-preview")
}

tasks {
    withType<JavaCompile> {
        compileJava {
            options.compilerArgs.addAll(listOf("--enable-preview"))
        }
    }

    withType<KotlinCompile> {
        compileKotlin {
        }
    }

    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<Tar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<Zip> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<Jar> {
        manifest {
            attributes["Main-Class"] = "com.mazekine.depool.scout.MainKt" // Set your main class here
        }
    }

    shadowJar {
        archiveFileName.set("depoolScoutFat.jar") // Customize the output JAR file name if needed
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        manifest {
            attributes["Main-Class"] = "com.mazekine.depool.scout.MainKt" // Set your main class here
        }

        mergeServiceFiles()
    }
}
