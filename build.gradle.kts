import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false

    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.vanniktechPublishing) apply false

    alias(libs.plugins.detekt)
    alias(libs.plugins.download)
    alias(libs.plugins.versions)
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
//        maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
    }

    // Gradle Dependency Check
    apply(plugin = "com.github.ben-manes.versions") // ./gradlew dependencyUpdates -Drevision=release
    val excludeVersionContaining = listOf("alpha", "eap") // example: "alpha", "beta"
    val ignoreArtifacts = listOf("material", "hilt-android") // some artifacts may be OK to check for "alpha"... add these exceptions here

    tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
        resolutionStrategy {
            componentSelection {
                all {
                    if (ignoreArtifacts.contains(candidate.module).not()) {
                        val rejected = excludeVersionContaining.any { qualifier ->
                            candidate.version.matches(Regex("(?i).*[.-]$qualifier[.\\d-+]*"))
                        }
                        if (rejected) {
                            reject("Release candidate")
                        }
                    }
                }
            }
        }
    }

    // ===== Detekt =====
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
    apply(plugin = rootProject.libs.plugins.download.get().pluginId)

    // download detekt config file
    tasks.register<de.undercouch.gradle.tasks.download.Download>("downloadDetektConfig") {
        download {
            onlyIf { !file("$projectDir/build/config/detektConfig.yml").exists() }
            src("https://mobile-cdn.churchofjesuschrist.org/android/build/detekt/detektConfig-20231101.yml")
            dest("$projectDir/build/config/detektConfig.yml")
        }
    }

    // make sure when running detekt, the config file is downloaded
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        this.jvmTarget = "17"
        dependsOn("downloadDetektConfig")
    }

    // ./gradlew detekt
    // ./gradlew detektDebug (support type checking)
    detekt {
        source.setFrom("src/main/kotlin", "src/commonMain/kotlin", "src/desktopMain/kotlin", "src/androidMain/kotlin")
        allRules = true // fail build on any finding
        buildUponDefaultConfig = true // preconfigure defaults
        config.setFrom(files("$projectDir/build/config/detektConfig.yml")) // point to your custom config defining rules to run, overwriting default behavior
        //    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        // ignore ImageVector files
        exclude("**/ui/compose/icons/**")

        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
            xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
            txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
