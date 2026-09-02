group = "zehen.myinsta2"

patches {
    about {
        name = "myinsta2 by zehen"
        description = "Morphe patches for the myinsta2 Instagram modification project"
        source = "git@github.com:somilkhan/Myinsta2.git"
        author = "zehen"
        contact = ""
        website = ""
        license = "GPLv3"
    }
}

val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Generate the patch index"
        dependsOn(build)
        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    publish {
        dependsOn("generatePatchesList")
    }
}
