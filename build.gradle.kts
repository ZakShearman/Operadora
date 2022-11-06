plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "pink.zak.minestom.operadora"
version = "1.3.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    api("com.github.Minestom:Minestom:42195c536b")
    api("com.influxdb:influxdb-client-java:6.5.0")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")
    implementation("com.typesafe:config:1.4.2")
    implementation("com.google.guava:guava:31.1-jre")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}


tasks {
    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "pink.zak.minestom.operadora.Operadora"
                )
            )
        }
    }
    processResources {
        expand(
            mapOf(
                "version" to version,
                "build_number" to (System.getenv("BUILD_NUMBER") ?: "0"),
                "commit_hash" to (System.getenv("BUILD_VCS_NUMBER") ?: "unknown")
            )
        )
    }
    named<Test>("test") {
        useJUnitPlatform()
    }
}

val mavenRepo = "https://mvn.zak.pink"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "pink.zak.minestom.operadora"
            artifactId = "operadora"

            from(components["java"])
        }
    }
    repositories {
        maven {
            val isSnapshot = hasProperty("BUILD_NUMBER")
            url = uri(mavenRepo + (if (isSnapshot) "/snapshots" else "/releases"))

            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}
