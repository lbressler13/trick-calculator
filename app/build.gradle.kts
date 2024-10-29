plugins {
    id("com.android.application")
    id("kotlin-android") apply true
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1" // ktlint
}

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

fun getIsEspresso(): Boolean {
    return project.findProperty("isEspresso") == "true"
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
    compileSdk = 34

    defaultConfig {
        applicationId = "xyz.lbres.trickcalculator"
        minSdk = 29 // maximum sdk available in tester used in github actions
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("int", "ESPRESSO_RETRIES", getEspressoRetries().toString())
        buildConfigField("Boolean", "IS_ESPRESSO", getIsEspresso().toString())

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
        buildConfig = true
    }

    testOptions {
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

    val androidxCoreVersion = "1.13.1"
    val appCompatVersion = "1.7.0"
    val constraintLayoutVersion = "2.1.4"
    val exactNumbersVersion = "1.0.2"
    val kotlinUtilsVersion = "1.4.4-android"
    val lifecycleVersion = "2.8.6"
    val materialVersion = "1.12.0"
    val navigationVersion = "2.8.3"

    val androidxJunitVersion = "1.2.1"
    val androidxTestRulesVersion = "1.6.1"
    val androidxTestRunnerVersion = "1.6.2"
    val espressoVersion = "3.6.1"
    val mockkVersion = "1.13.7"

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
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    androidTestImplementation("androidx.test:rules:$androidxTestRulesVersion")
    androidTestImplementation("androidx.test:runner:$androidxTestRunnerVersion") // needed to run on emulator
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-intents:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set("0.49.1")
    additionalEditorconfig.set(mapOf("max_line_length" to "120"))
}
