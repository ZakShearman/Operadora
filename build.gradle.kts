plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "pink.zak.minestom.operadora"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // modified minestom
    api("com.github.Minestom:Minestom:-SNAPSHOT")
    api("com.influxdb:influxdb-client-java:4.0.0")
    implementation("net.kyori:adventure-text-minimessage:4.10.0")
    implementation("com.typesafe:config:1.4.2")
    implementation("com.google.guava:guava:31.0.1-jre")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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
