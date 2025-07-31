plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.download)
    alias(libs.plugins.vanniktechPublishing)
    alias(libs.plugins.kotlin.android)
    signing
}

android {
    namespace = "org.dbtools.android.work"

    compileSdk = AndroidSdk.COMPILE

    defaultConfig {
        minSdk = AndroidSdk.MIN
        targetSdk = AndroidSdk.TARGET
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        abortOnError = true
        disable.addAll(listOf("InvalidPackage"))
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
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    api(libs.androidx.work.runtime)
    api(libs.androidx.room.runtime)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.core.ktx)

    // Test
//                testImplementation(libs.junit.jupiter)
//                testRuntimeOnly(libs.junit.engine)
//                testImplementation(libs.mockK)
}

// ===== TEST TASKS =====

// create JUnit reports
tasks.withType<Test> {
    useJUnitPlatform()
}

// ./gradlew clean build check publishToMavenLocal
// ./gradlew clean build check publishAllPublicationsToMavenCentralRepository
mavenPublishing {
    val version: String by project
    coordinates("org.dbtools", "workmanager-tools", version)
    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("WorkManager Tools")
        description.set("WorkManager Tools for Android is an library that makes it even easier to work with Google WorkManager Library")
        url.set("https://github.com/jeffdcamp/workmanager-tools")
        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("jcampbell")
                name.set("Jeff Campbell")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/jeffdcamp/workmanager-tools.git")
            developerConnection.set("scm:git:git@github.com:jeffdcamp/workmanager-tools.git")
            url.set("https://github.com/jeffdcamp/workmanager-tools")
        }
    }
}

signing {
    setRequired {
        findProperty("signing.keyId") != null
    }

    publishing.publications.all {
        sign(this)
    }
}
