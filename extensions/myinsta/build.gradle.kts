android {
    namespace = "dev.zehen.myinsta2.extension"

    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    compileOnly(project(":extensions:myinsta:stub"))
    compileOnly(libs.morphe.extensions.library)
}
