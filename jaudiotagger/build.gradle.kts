import java.util.Properties

plugins {
    java
    id("maven-publish")
    id("signing")
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

val secretPropsFile = rootProject.file("secrets.properties")
var secrets = Properties()
if (secretPropsFile.exists()) {
    secretPropsFile.inputStream().use {
        secrets.load(it)
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "io.github.chr56"
            artifactId = "jaudiotagger"
            version = "0.0.3"

            afterEvaluate {
                from(components.getByName("java"))
            }

            pom {
                name.set("JAudioTagger")
                description.set("Yet another fork of JAudioTagger of Kaned1as, which is a hard-fork of ijabs one")
                url.set("https://github.com/chr56/jaudiotagger/")

                licenses {
                    license {
                        name.set("LGPL-2.1")
                    }
                }
                developers {
                    developer {
                        id.set("chr_56")
                        name.set("chr56")
                    }
                }
                scm {
                    connection.set("https://github.com/chr56/jaudiotagger.git")
                    developerConnection.set("https://github.com/chr56/jaudiotagger.git")
                    url.set("https://github.com/chr56/jaudiotagger")
                }
            }
        }
    }
    repositories {
        maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2") {
            name = "MavenCentral"
            if (secretPropsFile.exists()) {
                credentials {
                    username = secrets["sonatype_username"] as String
                    password = secrets["sonatype_password"] as String
                }
            }
        }
    }
}

if (secretPropsFile.exists()) {
    signing {
        sign(publishing.publications)
        val key = File(secrets["signing_file"] as String).readText()
        useInMemoryPgpKeys(
            secrets["signing_key"] as String,
            key,
            secrets["signing_password"] as String
        )
    }
}
