import org.gradle.api.publish.maven.MavenPublication
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    `maven-publish`
    `signing`
}

group = "top.jayvin.dev"
val versionName = providers.environmentVariable("VERSION_NAME").orNull
    ?: providers.gradleProperty("VERSION_NAME").orNull
    ?: "0.1.0"
version = versionName

val isMac = System.getProperty("os.name").startsWith("Mac")
val artifactBase = "kx-essentials"

fun publicationArtifactId(name: String): String = when (name) {
    "kotlinMultiplatform" -> artifactBase
    // The Android publication is named androidRelease; the published artifact drops "release".
    "androidRelease" -> "$artifactBase-android"
    else -> "$artifactBase-${name.lowercase()}"
}

kotlin {
    explicitApi()

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        publishLibraryVariants("release")
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    js {
        browser {
            testTask {
                enabled = false
            }
        }
        nodejs()
    }

    // Apple targets require a macOS host. They stay available for local builds,
    // but CI and publishing intentionally run without the iOS targets.
    if (isMac) {
        iosArm64 {
            binaries.framework {
                baseName = "Kx"
            }
        }
        iosSimulatorArm64 {
            binaries.framework {
                baseName = "Kx"
            }
        }
        iosX64 {
            binaries.framework {
                baseName = "Kx"
            }
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "dev.kx"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("Kx Essentials")
            description.set(
                "Native-feeling Kotlin Multiplatform extensions for strings, collections, maps, numbers, results, and bits. Zero dependencies.",
            )
            url.set(
                providers.environmentVariable("GITHUB_REPOSITORY")
                    .map { "https://github.com/$it" }
                    .orElse("https://github.com/Jayvinz/kx-essentials")
                    .get(),
            )
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0")
                }
            }
            developers {
                developer {
                    id.set("Jayvinz")
                    name.set("Jayvin")
                }
            }
            scm {
                val repo = providers.environmentVariable("GITHUB_REPOSITORY").orElse("Jayvinz/kx-essentials").get()
                url.set("https://github.com/$repo")
                connection.set("scm:git:git://github.com/$repo.git")
                developerConnection.set("scm:git:ssh://git@github.com/$repo.git")
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(
                "https://maven.pkg.github.com/" +
                    providers.environmentVariable("GITHUB_REPOSITORY").orElse("Jayvinz/kx-essentials").get(),
            )
            credentials {
                username = providers.environmentVariable("GITHUB_ACTOR").orNull
                password = providers.environmentVariable("GITHUB_TOKEN").orNull
            }
        }
        maven {
            name = "local"
            url = layout.projectDirectory.dir("../local-repo").asFile.toURI()
        }
    }
}

val signingKey = providers.environmentVariable("SIGNING_KEY").orNull
signing {
    isRequired = !signingKey.isNullOrBlank()
    if (!signingKey.isNullOrBlank()) {
        useInMemoryPgpKeys(
            signingKey,
            providers.environmentVariable("SIGNING_PASSWORD").orNull ?: "",
        )
    }
}
// Publication tweaks have to run after the Kotlin/Android plugins finish configuring their
// publications: the Android plugin sets its own artifactId ("<project>-android") late, and
// adding artifacts after signing has been wired would leave them unsigned.
afterEvaluate {
    publishing.publications.withType<MavenPublication>().configureEach {
        artifactId = publicationArtifactId(name)
        // Maven Central requires a javadoc artifact next to every published artifact. Kotlin
        // Multiplatform has no Javadoc toolchain for its metadata, JS, and Android publications,
        // so publish a placeholder jar (explicitly allowed by Central) containing the README.
        // One task per publication keeps the signed jar paths distinct; a shared task would make
        // every sign task write the same .asc file.
        val javadocJar = tasks.register<Jar>("${name}JavadocJar") {
            archiveBaseName.set(artifactId)
            archiveClassifier.set("javadoc")
            from(rootProject.file("README.md"))
        }
        artifact(javadocJar)
        if (!signingKey.isNullOrBlank()) {
            signing.sign(this)
        }
    }
}
