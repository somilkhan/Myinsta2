plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.zehen.myinsta2.extension.stub"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
