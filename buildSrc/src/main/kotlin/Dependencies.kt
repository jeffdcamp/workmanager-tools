const val KOTLIN_VERSION = "1.4.20"
private const val COROUTINES_VERSION = "1.4.2"
const val DAGGER_HILT_VERSION = "2.31.2-alpha"

object Deps {
    // Android (https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-master-dev/buildSrc/src/main/kotlin/androidx/build/dependencies/Dependencies.kt)
    const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:1.2.0"
    const val ANDROIDX_RECYCLERVIEW = "androidx.recyclerview:recyclerview:1.1.0"
    const val ANDROIDX_CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val ANDROIDX_ACTIVITY_KTX = "androidx.activity:activity-ktx:1.2.0-rc01"
    const val ANDROIDX_FRAGMENT_KTX = "androidx.fragment:fragment-ktx:1.3.0-rc02"

    private const val LIFECYCLE_VERSION = "2.3.0-rc01"
    const val ARCH_LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:$LIFECYCLE_VERSION"
    const val ARCH_LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION"

    const val ARCH_WORK_RUNTIME = "androidx.work:work-runtime-ktx:2.5.0"

    // Code
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES_VERSION"
    const val TIMBER = "com.jakewharton.timber:timber:4.7.1"

    // Inject
    private const val ANDROIDX_HILT_VERSION = "1.0.0-alpha03"
    const val HILT = "com.google.dagger:hilt-android:$DAGGER_HILT_VERSION"
    const val HILT_COMPILER = "com.google.dagger:hilt-android-compiler:$DAGGER_HILT_VERSION"
    const val HILT_TESTING = "com.google.dagger:hilt-android-testing:$DAGGER_HILT_VERSION" // integration tests ONLY (does not support JVM tests)
    const val ANDROIDX_HILT_WORK = "androidx.hilt:hilt-work:$ANDROIDX_HILT_VERSION"
    const val ANDROIDX_HILT_VIEWMODEL = "androidx.hilt:hilt-lifecycle-viewmodel:$ANDROIDX_HILT_VERSION"
    const val ANDROIDX_HILT_COMPILER = "androidx.hilt:hilt-compiler:$ANDROIDX_HILT_VERSION"

    // Standard dagger is needed for unit tests ONLY
    private const val DAGGER_VERSION = "2.31.2"
    const val DAGGER = "com.google.dagger:dagger:$DAGGER_VERSION"
    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:$DAGGER_VERSION"

    // Database
    private const val ROOM_VERSION = "2.3.0-beta01"
    const val ARCH_ROOM_RUNTIME = "androidx.room:room-runtime:$ROOM_VERSION"

    // Test
    private const val JUNIT_VERSION = "5.7.0"
    const val TEST_JUNIT = "org.junit.jupiter:junit-jupiter:$JUNIT_VERSION"
    const val TEST_JUNIT_ENGINE = "org.junit.jupiter:junit-jupiter-engine:$JUNIT_VERSION"

    const val TEST_RUNNER = "androidx.test:runner:1.2.0"
    const val TEST_RULES = "androidx.test:rules:1.2.0"
    const val TEST_ANDROIDX_JUNIT = "androidx.test.ext:junit:1.1.1"
    const val TEST_MOCKITO_CORE = "org.mockito:mockito-core:3.7.7"
    const val TEST_XERIAL_SQLITE = "org.xerial:sqlite-jdbc:3.27.2.1"
    const val TEST_ARCH_ROOM_TESTING = "androidx.room:room-testing:$ROOM_VERSION"
    const val TEST_KOTLIN_COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINES_VERSION"
    const val TEST_MOCKITO_KOTLIN = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
}