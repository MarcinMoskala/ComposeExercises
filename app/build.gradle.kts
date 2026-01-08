plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.screenshot)
    alias(libs.plugins.stability.analyzer)
    kotlin("plugin.serialization") version "2.1.10"
    id("kotlin-parcelize")
}

android {
    namespace = "com.marcinmoskala.composeexercises"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.marcinmoskala.composeexercises"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    sourceSets {
        maybeCreate("screenshotTest").manifest.srcFile("src/screenshotTest/AndroidManifest.xml")
        maybeCreate("screenshotTestDebug").manifest.srcFile("src/screenshotTest/AndroidManifest.xml")
    }

    // Needed by Screenshot tests
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

composeCompiler {
//    stabilityConfigurationFiles = listOf(rootProject.layout.projectDirectory.file("stability_config.conf"))
//    enableStrongSkippingMode = false
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}

tasks.withType<com.android.build.gradle.tasks.GenerateTestConfig>().configureEach {
    if (name.contains("ScreenshotTest", ignoreCase = true)) {
        enabled = false
    }
}

tasks.withType<Test>().configureEach {
    if (name.contains("ScreenshotTest", ignoreCase = true)) {
        enabled = false
    }
}

dependencies {
    implementation(libs.coil3)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.fragment.compose)
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    screenshotTestImplementation(libs.androidx.compose.ui.tooling)
}