@file:Suppress("UnstableApiUsage")

import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("fabric-loom") version "1.0-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.7.3"

    java
    `maven-publish`
}

version = rootProject.property("mod_version").toString()
group = rootProject.property("maven_group").toString()

repositories {
    mavenCentral()
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://maven.shedaniel.me")
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
    mappings(project.the<LoomGradleExtensionAPI>().officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric_api_version")}")

    // Dev mods
    modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:1.1.0")
    modRuntimeOnly("maven.modrinth:lazydfu:0.1.3")
}

tasks {
    named<JavaCompile>("compileJava") {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    named<ProcessResources>("processResources") {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    remapJar {
        dependsOn("build")
        archiveClassifier.set(null as String?)
    }

    jar {
        archiveClassifier.set("dev")
    }
}

configure<BasePluginExtension> {
    archivesName.set(rootProject.property("archives_base_name").toString())
}

configure<LoomGradleExtensionAPI> {
    runConfigs.remove(runConfigs["server"])
}

configure<JavaPluginExtension> {
    withSourcesJar()
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenFabric") {
            from(components["java"])
            artifactId = "${rootProject.property("archives_base_name")}-${project.name}"
        }
    }
}
