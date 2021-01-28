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
    compileSdkVersion(AndroidSdk.COMPILE)

    defaultConfig {
        minSdkVersion(AndroidSdk.MIN)
        targetSdkVersion(AndroidSdk.TARGET)

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
        dataBinding = true
    }

    lintOptions {
        isAbortOnError = true
        disable("InvalidPackage")
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = " DEV"
            applicationIdSuffix = ".dev"
        }
        getByName("release") {
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
    implementation(Deps.ANDROIDX_APPCOMPAT)
    implementation(Deps.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(Deps.ANDROIDX_RECYCLERVIEW)
    implementation(Deps.ANDROIDX_ACTIVITY_KTX)
    implementation(Deps.ANDROIDX_FRAGMENT_KTX)

    // Code
    implementation(Deps.COROUTINES)
    implementation(Deps.TIMBER)

    // Inject
    implementation(Deps.HILT)
    kapt(Deps.HILT_COMPILER)
    implementation(Deps.ANDROIDX_HILT_WORK)
    implementation(Deps.ANDROIDX_HILT_VIEWMODEL)
    kapt(Deps.ANDROIDX_HILT_COMPILER)

    // === Android Architecture Components ===
    implementation(Deps.ARCH_LIFECYCLE_RUNTIME)
    implementation(Deps.ARCH_LIFECYCLE_VIEWMODEL)

    // Dagger 2
    implementation(Deps.DAGGER)
    kapt(Deps.DAGGER_COMPILER)

}

// ===== TEST TASKS =====

// create JUnit reports
tasks.withType<Test> {
    useJUnitPlatform()
}
