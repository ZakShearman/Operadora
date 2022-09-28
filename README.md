# Operadora

Operadora is a a Minestom server implementation with basic features, some utilities and full modularity. <3

[![Build Status](https://ci.zak.pink/app/rest/builds/buildType:(id:Operadora_Build)/statusIcon)](https://ci.zak.pink/buildConfiguration/Operadora_Build?guest=1)
![GitHub all releases](https://img.shields.io/github/downloads/ZakShearman/Operadora/total)
![License](https://img.shields.io/github/license/ZakShearman/mc-tower-defence)

## Requirements

  - Java 17+
  - 256MB RAM

## Installation

  - Download the latest release (stable) or [build from the CI](https://ci.zak.pink/buildConfiguration/Operadora_Build?guest=1) (development)
  - Run the jar using `java -jar fileName.jar`

## Features

  - Host Support (for hosts that don't have minestom support)
  - Metrics/Monitoring support via InfluxDB
  - Easy server configuration via the [server-properties.conf](https://github.com/ZakShearman/Operadora/blob/master/src/main/resources/server-properties.conf) file


## Importing from Maven

If you are using a pre-release build, make sure you use the build number from [TeamCity](https://ci.zak.pink/buildConfiguration/Operadora_Build?guest=1)

```gradle
repositories {
  maven("https://mvn.zak.pink/releases") // release builds
  maven("https://mvn.zak.pink/shapshots") // pre-release builds
}

dependencies {
  compileOnly("pink.zak.minestom.operadora:operadora:1.2.0") // release builds
  compileOnly("pink.zak.minestom.operadora:operadora:26") // pre-release builds (see TeamCity CI)
}
```

  
## Modifying the JAR

  - Clone the repository using `git clone https://github.com/ZakShearman/Operadora.git`
  - Open the code in your IDE and import the Gradle project
  - Run the `shadowJar` gradle task with your IDE or using `./gradlew shadowJar`. The JAR will be created in the `builds/libs` folder
