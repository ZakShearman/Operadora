import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "pink.zak.minestom.operadora"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // modified minestom
    implementation("com.github.ZakShearman:Minestom:12bc35f89")
    implementation("com.typesafe:config:1.4.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}


tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes(
                "Main-Class" to "pink.zak.minestom.operadora.Operadora"
            )
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}