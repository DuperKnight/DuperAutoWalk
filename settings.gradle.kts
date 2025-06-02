pluginManagement {
    repositories {
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net/releases")
        }
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6"
}

stonecutter {
    create(rootProject) {
        versions("1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5")
        vcsVersion = "1.21.5"
    }
}

rootProject.name = "DuperAutoWalk"
