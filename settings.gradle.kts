dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven(url = "https://jitpack.io")
    }
}
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

rootProject.buildFileName = "build.gradle.kts"

rootProject.name = "jaudiotagger"

include(":jaudiotagger")

