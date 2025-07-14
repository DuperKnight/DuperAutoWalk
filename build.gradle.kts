import org.gradle.api.tasks.Copy

plugins {
    id("dev.kikugie.stonecutter")
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

version = "${project.version}+${stonecutter.current.project}"

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.jar.get().archiveFile)
    into(rootProject.layout.buildDirectory.dir("chiseled-jars"))
    dependsOn("build")

    rename { originalName ->
        val baseName = originalName.substringBeforeLast(".jar")
        "${baseName}-neoforge.jar"
    }
}

tasks.register("collectChiseledJars") {
    group = "distribution"
    description = "Collect chiseled jars from all versions (alias for chiseledBuildAndCollect)"
    dependsOn(":chiseledBuildAndCollect")
}

// Utility to map Minecraft project versions into versionRange strings
fun getMcVersionRange(mcVersion: String): String = when(mcVersion) {
    "1.21", "1.21.1" -> "[1.21,1.21.1]"
    "1.21.2", "1.21.3" -> "[1.21.2,1.21.3]"
    "1.21.4" -> "[1.21.4]"
    "1.21.5" -> "[1.21.5]"
    "1.21.6", "1.21.7" -> "[1.21.6,1.21.7]"
    else -> mcVersion
}

tasks.processResources {
    filesMatching("**/META-INF/neoforge.mods.toml") {
        expand(
            "modVersion" to project.version,
            "mcVersionRange" to getMcVersionRange(stonecutter.current.project)
        )
    }
}
