plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.7"

stonecutter registerChiseled tasks.register("chiseledBuildAndCollect", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}