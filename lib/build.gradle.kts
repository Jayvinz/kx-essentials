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
val artifactBase = "kx"

fun publicationArtifactId(name: String): String =
    if (name == "kotlinMultiplatform") artifactBase else "$artifactBase-${name.lowercase()}"

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

    // Apple targets require a macOS host. Keeping them conditional lets Windows
    // and Linux contributors run the JVM/JS/Android build without Konan errors.
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
        artifactId = publicationArtifactId(name)
        pom {
            name.set("Kx")
            description.set(
                "Small, native-feeling Kotlin Multiplatform extensions for common collection, text, number, result, and bit operations.",
            )
            url.set(
                providers.environmentVariable("GITHUB_REPOSITORY")
                    .map { "https://github.com/$it" }
                    .orElse("https://github.com/Jayvinz/kx")
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
                val repo = providers.environmentVariable("GITHUB_REPOSITORY").orElse("Jayvinz/kx").get()
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
                    providers.environmentVariable("GITHUB_REPOSITORY").orElse("Jayvinz/kx").get(),
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
if (!signingKey.isNullOrBlank()) {
    publishing.publications.withType<MavenPublication>().configureEach {
        signing.sign(this)
    }
}

afterEvaluate {
    publishing.publications.withType<MavenPublication>().configureEach {
        if (name == "androidRelease") {
            artifactId = "$artifactBase-android"
        }
    }
}
