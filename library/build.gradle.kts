import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    `maven-publish`
    signing
    kotlin("android")
    kotlin("kapt")
}

// Kotlin Libraries targeting Java8 bytecode can cause the following error (such as okHttp 4.x):
// "Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6. Please specify proper '-jvm-target' option"
// The following is added to allow the Kotlin Compiler to compile properly
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

android {
    compileSdk = AndroidSdk.COMPILE

    defaultConfig {
        minSdk = AndroidSdk.MIN
        targetSdk = AndroidSdk.TARGET
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lint {
        isAbortOnError = true
        disable("InvalidPackage")
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-module-name", Pom.LIBRARY_ARTIFACT_ID)
    }
}

dependencies {
    api(libs.androidx.appcompat)
    api(libs.androidx.recyclerview)
    api(libs.androidx.fragment)
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.work.runtime)
    api(libs.androidx.room.runtime)
    api(libs.androidx.constraintlayout)
    api(libs.timber)

    // Test
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.mockK)
    testImplementation(libs.truth)
}

// ===== TEST TASKS =====

// create JUnit reports
tasks.withType<Test> {
    useJUnitPlatform()
}

// ===== Maven Deploy =====

// ./gradlew clean assembleRelease publishMavenPublicationToMavenLocal
// ./gradlew clean assembleRelease publishMavenPublicationToMavenCentralRepository

tasks.register<Jar>("sourcesJar") {
//    from(android.sourceSets.getByName("main").java.sourceFiles)
    from(project.the<BaseExtension>().sourceSets["main"].java.srcDirs)
    archiveClassifier.set("sources")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = Pom.GROUP_ID
            artifactId = Pom.LIBRARY_ARTIFACT_ID
            version = Pom.VERSION_NAME
            artifact(tasks["sourcesJar"])
            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
            pom {
                name.set(Pom.POM_NAME)
                description.set(Pom.POM_DESCRIPTION)
                url.set(Pom.URL)
                licenses {
                    license {
                        name.set(Pom.LICENCE_NAME)
                        url.set(Pom.LICENCE_URL)
                        distribution.set(Pom.LICENCE_DIST)
                    }
                }
                developers {
                    developer {
                        id.set(Pom.DEVELOPER_ID)
                        name.set(Pom.DEVELOPER_NAME)
                    }
                }
                scm {
                    url.set(Pom.SCM_URL)
                    connection.set(Pom.SCM_CONNECTION)
                    developerConnection.set(Pom.SCM_DEV_CONNECTION)
                }

                // add dependencies to pom.xml
                pom.withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations.implementation.get().allDependencies.forEach {
                        val dependencyNode = dependenciesNode.appendNode("dependency")
                        dependencyNode.appendNode("groupId", it.group)
                        dependencyNode.appendNode("artifactId", it.name)
                        dependencyNode.appendNode("version", it.version)
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "MavenCentral"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                val sonatypeNexusUsername: String? by project
                val sonatypeNexusPassword: String? by project
                username = sonatypeNexusUsername ?: ""
                password = sonatypeNexusPassword ?: ""
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}