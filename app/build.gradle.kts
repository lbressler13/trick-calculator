plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0" // ktlint
}
apply(plugin = "kotlin-android")

val githubUsername: String? = project.findProperty("github.username")?.toString() ?: System.getenv("USERNAME")
val githubPassword: String? = project.findProperty("github.token")?.toString() ?: System.getenv("ACCESS_TOKEN")

repositories {
    // general repositories
    google()
    mavenCentral()

    // kotlin utils
    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://maven.pkg.github.com/lbressler13/kotlin-utils")
                credentials {
                    username = githubUsername
                    password = githubPassword
                }
            }
        }
        filter {
            includeModule("xyz.lbres", "kotlin-utils")
        }
    }

    // exact numbers
    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://maven.pkg.github.com/lbressler13/exact-numbers")
                credentials {
                    username = githubUsername
                    password = githubPassword
                }
            }
        }
        filter {
            includeModule("xyz.lbres", "exact-numbers")
        }
    }
}

fun getEspressoRetries(): Int {
    val defaultRetries = 0

    return if (project.hasProperty("espressoRetries")) {
        val espressoRetries: String? by project
        espressoRetries?.toIntOrNull() ?: defaultRetries
    } else {
        defaultRetries
    }
}

android {
    namespace = "xyz.lbres.trickcalculator"
    compileSdk = 31

    defaultConfig {
        applicationId = "xyz.lbres.trickcalculator"
        minSdk = 29 // maximum sdk available in tester used in github actions
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("int", "ESPRESSO_RETRIES", getEspressoRetries().toString())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += "type"
    productFlavors {
        create("dev") {
            dimension = "type"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }

        create("final") {
            dimension = "type"
            versionNameSuffix = "-final"
        }
    }

    sourceSets.getByName("main") {
        java.setSrcDirs(listOf("src/main/kotlin"))
    }

    sourceSets.getByName("dev") {
        java.setSrcDirs(listOf("src/dev/kotlin"))
    }

    sourceSets.getByName("final") {
        java.setSrcDirs(listOf("src/final/kotlin"))
    }

    sourceSets.getByName("test") {
        java.setSrcDirs(listOf("src/test/kotlin"))
    }

    sourceSets.getByName("androidTest") {
        kotlin.setSrcDirs(listOf("src/espresso/kotlin"))
        java.setSrcDirs(listOf("src/espresso/kotlin"))
    }

    sourceSets.getByName("androidTestDev") {
        kotlin.setSrcDirs(listOf("src/espressoDev/kotlin"))
        java.setSrcDirs(listOf("src/espressoDev/kotlin"))
    }

    sourceSets.getByName("androidTestFinal") {
        kotlin.setSrcDirs(listOf("src/espressoFinal/kotlin"))
        java.setSrcDirs(listOf("src/espressoFinal/kotlin"))
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true // mocks android Log
        animationsDisabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    val kotlinVersion: String by rootProject.extra

    val androidxCoreVersion = "1.8.0"
    val appCompatVersion = "1.4.1"
    val constraintLayoutVersion = "2.1.4"
    val lifecycleVersion = "2.5.1"
    val materialVersion = "1.6.1"
    val navigationVersion = "2.5.3"

    val exactNumbersVersion = "0.1.0"
    val kotlinUtilsVersion = "1.2.0"

    val androidxJunitVersion = "1.1.4"
    val androidxTestVersion = "1.5.0"
    val espressoVersion = "3.4.0"
    val junitVersion = "4.13.2"

    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("xyz.lbres:exact-numbers:$exactNumbersVersion")
    implementation("xyz.lbres:kotlin-utils:$kotlinUtilsVersion")

    // testing
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    androidTestImplementation("androidx.test:rules:$androidxTestVersion")
    androidTestImplementation("androidx.test:runner:$androidxTestVersion") // needed to run on emulator
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-intents:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
}
