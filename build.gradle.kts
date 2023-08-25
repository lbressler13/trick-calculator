// main dependencies
val androidxCoreVersion by extra { "1.8.0" }
val appCompatVersion by extra { "1.4.1" }
val constraintLayoutVersion by extra { "2.1.4" }
val kotlinUtilsVersion by extra { "1.1.0" }
val lifecycleVersion by extra { "2.5.1" }
val materialVersion by extra { "1.6.1" }
val navigationVersion by extra { "2.5.3" }

// test dependencies
val androidxJunitVersion by extra { "1.1.4" }
val androidxTestVersion by extra { "1.5.0" }
val espressoVersion by extra { "3.4.0" }
val junitVersion by extra { "4.13.2" }

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    val kotlinVersion by extra { "1.6.20" }
    val gradleVersion = "7.3.1"

    // only project-level dependencies, app-specific dependencies should go in application build files
    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        // general repositories
        google()
        mavenCentral()

        // kotlin utils
        val githubUsername: String? = project.findProperty("gpr.user")?.toString() ?: System.getenv("USERNAME")
        val githubPassword: String? = project.findProperty("gpr.key")?.toString() ?: System.getenv("TOKEN")
        maven {
            url = uri("https://maven.pkg.github.com/lbressler13/kotlin-utils")
            credentials {
                username = githubUsername
                password = githubPassword
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
