plugins {
    java
    id("maven-publish")
}

java {
    sourceSets {
        main {
            java.srcDir("src")
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
	implementation("com.squareup.okio:okio:1.17.3")
    compileOnly("com.google.android:android:4.1.1.4")
}
publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                from(components.getByName("java"))
            }
            // groupId = ""
            // artifactId = ""
            // version = ""
        }
    }
}