group = "dev.zehen.myinsta2"

patches {
    about {
        name = "myinsta2 by zehen"
        description = "Instagram patches maintained for the myinsta2 project."
        source = "https://github.com/somilkhan/Myinsta2"
        author = "zehen"
        contact = "https://github.com/somilkhan/Myinsta2/issues"
        website = "https://github.com/somilkhan/Myinsta2"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly("com.google.code.gson:gson:2.13.1")
    patchListGeneratorClasspath("com.google.code.gson:gson:2.13.1")
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Generate Morphe patch metadata"
        dependsOn(build)
        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    named("publish") {
        dependsOn("generatePatchesList")
    }
}
