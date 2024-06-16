plugins {
    kotlin("jvm") version "2.0.0"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.mazekine.everscale.addressMiner"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("tech.deplant.java4ever:java4ever-framework:3.0.0")
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(22)
}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf("--enable-preview")
}

tasks {
    withType<JavaCompile> {
        compileJava {
            options.compilerArgs.addAll(listOf("--enable-preview"))
        }
    }

    shadowJar {
        archiveFileName.set("addressMinerFat.jar") // Customize the output JAR file name if needed
        manifest {
            attributes["Main-Class"] = "MainKt" // Set your main class here
        }
        mergeServiceFiles()
    }
}
