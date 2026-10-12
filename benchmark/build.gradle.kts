plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.marcinmoskala.composeexercises.benchmark"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Records composable functions as trace sections (needs runtime-tracing in :app)
        testInstrumentationRunnerArguments["androidx.benchmark.fullTracing.enable"] = "true"
        // Numbers from an emulator are not reliable, but traces are still useful for this exercise.
        // Remove to get the warning back when benchmarking on a real device.
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    buildTypes {
        create("benchmark") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}

// A self-instrumenting test module does not install the target app on its own
tasks.matching { it.name == "connectedBenchmarkAndroidTest" }.configureEach {
    dependsOn(":app:installBenchmark")
}
