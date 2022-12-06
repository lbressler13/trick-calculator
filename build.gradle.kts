buildscript {
    repositories {
        google()
        mavenCentral()
    }

    val kotlinVersion = "1.6.20"
    val gradleVersion = "7.0.4"

    // only project-level dependencies, app-specific dependencies should go in application build files
    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
