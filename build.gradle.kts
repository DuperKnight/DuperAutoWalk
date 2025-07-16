plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("maven-publish")
    id("dev.kikugie.stonecutter") version "0.6"
}

version = "${property("version")}+${stonecutter.current.project}"
group = project.property("maven_group").toString()

base {
    archivesName.set(project.property("archives_base_name").toString())
}

repositories {
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven {
        name = "Xander Maven"
        url = uri("https://maven.isxander.dev/releases")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${stonecutter.current.project}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("com.terraformersmc:modmenu:${property("modmenu_version")}")
    modImplementation("dev.isxander:yet-another-config-lib:${property("yacl_version")}")
}

tasks.processResources {
    val loaderVersion = findProperty("loader_version") as? String
        ?: throw GradleException("Property loader_version is not defined")

    val minecraftVersionRange = when (stonecutter.current.project) {
        "1.21.1" -> ">=1.21 <1.21.2"
        "1.21.3" -> ">=1.21.2 <1.21.4"
        "1.21.4" -> "1.21.4"
        "1.21.5" -> "1.21.5"
        "1.21.7" -> ">=1.21.6 <1.21.8"
        else -> stonecutter.current.project
    }

    inputs.property("minecraft_version", minecraftVersionRange)
    inputs.property("loader_version", loaderVersion)
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand(
            "minecraft_version" to minecraftVersionRange,
            "version" to version,
            "loader_version" to loaderVersion
        )
    }
}

val targetJavaVersion = 17
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    withSourcesJar()
    val javaVersion = JavaVersion.VERSION_21
    targetCompatibility = javaVersion
    sourceCompatibility = javaVersion
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.property("archives_base_name")}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        // Add your publishing repositories here.
    }
}

loom {
    runConfigs.configureEach {
        runDir = "../../run"
    }
}


tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.jar.get().archiveFile)
    into(rootProject.layout.buildDirectory.dir("chiseled-jars"))
    dependsOn("build")

    rename { originalName ->
        val baseName = originalName
            .substringBeforeLast(".jar")
            .replace("-dev", "")
        "$baseName-fabric.jar"
    }
}

tasks.register("collectChiseledJars") {
    group = "distribution"
    description = "Collect chiseled jars from all versions (alias for chiseledBuildAndCollect)"
    dependsOn(":chiseledBuildAndCollect")
}