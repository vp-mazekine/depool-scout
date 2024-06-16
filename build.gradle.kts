plugins {
    kotlin("jvm") version "1.9.22"
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
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
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
