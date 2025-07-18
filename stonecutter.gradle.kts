plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.8"

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}