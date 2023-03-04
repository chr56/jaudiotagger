plugins {
    java
    id("maven-publish")
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
    sourceSets {
        main {
            java.srcDir("src")
        }
    }
    withSourcesJar()
    //withJavadocJar()
}

tasks.withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
}
//tasks.withType(Javadoc::class.java) {
//    options.encoding = "UTF-8"
//}

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
            groupId = "io.github.chr56.jaudiotagger"
            artifactId = "jaudiotagger"
            version = "0.0.2"
        }
    }
}