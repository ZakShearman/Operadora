import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.lang.System

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "pink.zak.minestom.operadora"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/") // todo minimessage? plz fix
}

dependencies {
    // modified minestom
    api("com.github.ZakShearman:Minestom:2034ee1f2")
    api("com.influxdb:influxdb-client-java:4.0.0")
    api("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT")
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
    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

val mavenRepo = "https://mvn.zak.pink"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "pink.zak.minestom.operadora"
            artifactId = "operadora"

            val buildNumber = System.getenv("BUILD_NUMBER");
            version = if (buildNumber == null) "1.0" else buildNumber

            from(components["java"])
        }
    }
    repositories {
        maven {
            var isSnapshot = System.getenv("BUILD_NUMBER") != null;
            url = uri(mavenRepo + (if (isSnapshot) "/snapshots" else "/releases"))

            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}