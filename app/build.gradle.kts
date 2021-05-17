plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

// Kotlin Libraries targeting Java8 bytecode can cause the following error (such as okHttp 4.x):
// "Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6. Please specify proper '-jvm-target' option"
// The following is added to allow the Kotlin Compiler to compile properly
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

android {
    compileSdk = AndroidSdk.COMPILE

    defaultConfig {
        minSdk = AndroidSdk.MIN
        targetSdk = AndroidSdk.TARGET

        versionCode = 1000
        versionName = "1.0.0"

        // Espresso
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }

    lint {
        isAbortOnError = true
        disable("InvalidPackage")
    }

    buildTypes {
        debug {
            versionNameSuffix = " DEV"
            applicationIdSuffix = ".dev"
        }
        release {
            versionNameSuffix = ""
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
        getByName("test") {
            java.srcDir("src/test/kotlin")
        }
        getByName("androidTest") {
            assets.srcDir("$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(project(":library"))

    // Android
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)

    // Code
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.timber)

    // Inject
    kapt(libs.google.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.google.hilt.library)
    implementation(libs.androidx.hilt.work)

    // === Android Architecture Components ===
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}

// ===== TEST TASKS =====

// create JUnit reports
tasks.withType<Test> {
    useJUnitPlatform()
}
