plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("maven-publish")
}

version = project.property("version").toString()
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
    // The minecraft configuration is defined by the fabric-loom plugin.
    minecraft("com.mojang:minecraft:${stonecutter.current.project}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("com.terraformersmc:modmenu:${property("modmenu_version")}")
    modImplementation("dev.isxander:yet-another-config-lib:${property("yacl_version")}")
}

tasks.processResources {
    val loaderVersion = project.findProperty("loader_version") as? String
        ?: throw GradleException("Property loader_version is not defined")

    inputs.property("minecraft_version", stonecutter.current.project)
    inputs.property("loader_version", loaderVersion)
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand(
            "minecraft_version" to stonecutter.current.version,
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
    val javaVersion = if (stonecutter.eval(project.property("minecraft_version").toString(), ">=1.20.5")) {
        JavaVersion.VERSION_21
    } else {
        JavaVersion.VERSION_17
    }
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