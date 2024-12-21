import io.papermc.hangarpublishplugin.model.Platforms

plugins {
    id("java")
    kotlin("jvm")
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
    id("com.modrinth.minotaur") version "2.8.7"
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
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain(17)
}

hangarPublish {
    publications.register("plugin") {
        version.set(project.version.toString())
        channel.set("Release")
        id.set("BreweryGuiEditor")
        apiKey.set(System.getenv("HANGAR_TOKEN"))
        platforms {
            register(Platforms.PAPER) {
                jar.set(tasks.jar.flatMap { it.archiveFile })
                platformVersions.set(listOf("1.20.x", "1.21.x"))
            }
        }
        changelog.set(readChangeLog())
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("BreweryGuiEditor") // This can be the project ID or the slug. Either will work!
    versionNumber.set(project.version.toString())
    versionType.set("release") // This is the default -- can also be `beta` or `alpha`
    uploadFile.set(tasks.jar)
    loaders.addAll("paper", "purpur", "folia")
    gameVersions.addAll("1.20.x", "1.21.x")
    changelog.set(readChangeLog())
}

fun readChangeLog(): String {
    val text: String = System.getenv("CHANGELOG") ?: file("CHANGELOG.md").run {
        if (exists()) readText() else "No Changelog found."
    }
    return text.replace("\${version}", project.version.toString())
}