plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.haloai.app"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "0.1"
    }
}

dependencies {
    // Add Compose, AndroidX, Room, Coroutines dependencies in real implementation
}
