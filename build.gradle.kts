buildscript {
    repositories {
        google()
        mavenCentral()
    }

    val kotlinVersion by extra { "1.9.0" }
    val gradleVersion = "8.1.2"

    // only project-level dependencies, app-specific dependencies should go in application build files
    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
