import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Jar

plugins {
    id("net.neoforged.gradle.userdev") version "7.0.163"
    id("maven-publish")
}

version = project.property("version").toString()
group = project.property("maven_group").toString()

base {
    archivesName.set(project.property("archives_base_name").toString())
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

repositories {
    mavenCentral()
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
    maven {
        name = "Xander Maven"
        url = uri("https://maven.isxander.dev/releases")
    }
    maven {
        name = "TDark's Maven"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

configurations.all {
    resolutionStrategy {
        force("org.ow2.asm:asm-analysis:9.7")
        force("org.ow2.asm:asm-commons:9.7")
        force("org.ow2.asm:asm-tree:9.7")
        force("org.ow2.asm:asm-util:9.7")
        force("org.ow2.asm:asm:9.7")
    }
}

dependencies {
    implementation("net.neoforged:neoforge:${property("neoforge_version")}")
    implementation("dev.isxander:yet-another-config-lib:${property("yacl_version")}")
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.property("archives_base_name")}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.jar.get().archiveFile)
    into(rootProject.layout.buildDirectory.dir("chiseled-jars"))
    dependsOn("build")

    rename { originalName ->
        val mcVersion = stonecutter.current.project
        val baseName = originalName.substringBeforeLast(".jar")
        "${baseName}-${mcVersion}+neoforge.jar"
    }
}

tasks.register("collectChiseledJars") {
    group = "distribution"
    description = "Collect chiseled jars from all versions (alias for chiseledBuildAndCollect)"
    dependsOn(":chiseledBuildAndCollect")
}
