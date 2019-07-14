@file:Suppress("MemberVisibilityCanBePrivate")

const val KOTLIN_VERSION = "1.3.41"

object AndroidSdk {
    const val MIN = 21
    const val COMPILE = 29
    const val TARGET = COMPILE
}

object Pom {
    const val GROUP_ID = "org.dbtools"
    const val VERSION_NAME = "1.8.0"
    const val POM_DESCRIPTION = "WorkManager Tools"

    const val URL = "https://github.com/jeffdcamp/workmanager-tools/"
    const val SCM_URL = "https://github.com/jeffdcamp/workmanager-tools/"
    const val SCM_CONNECTION = "scm:git:git://github.com/jeffdcamp/workmanager-tools.git"
    const val SCM_DEV_CONNECTION = "scm:git:git@github.com:jeffdcamp/workmanager-tools.git"

    const val LICENCE_NAME = "The Apache Software License, Version 2.0"
    const val LICENCE_URL = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    const val LICENCE_DIST = "repo"

    const val DEVELOPER_ID = "jcampbell"
    const val DEVELOPER_NAME = "Jeff Campbell"

    const val LIBRARY_ARTIFACT_ID = "workmanager-tools"
}

