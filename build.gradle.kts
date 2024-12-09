plugins {
    id("java")
    kotlin("jvm")
}

group = "dev.jsinco.brewery"
version = "BX3.4.3"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.jsinco.dev/releases")
}

dependencies {
    compileOnly("com.dre.brewery:BreweryX:3.4.3-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly(kotlin("stdlib-jdk8"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain(21)
}