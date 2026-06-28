plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.11"

tasks.register("chiseledBuildAndCollect") {
    group = "project"
    dependsOn(":1.21.8:buildAndCollect", ":1.21.9:buildAndCollect", ":1.21.11:buildAndCollect")
}
