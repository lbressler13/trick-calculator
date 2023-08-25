plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0" // ktlint
}
apply(plugin = "kotlin-android")

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
    compileSdk = 33

    defaultConfig {
        applicationId = "xyz.lbres.trickcalculator"
        // TODO
        // minSdk = 29 // maximum sdk available in tester used in github actions
        minSdk = 33
        targetSdk = 33
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

    val androidxCoreVersion: String by rootProject.extra
    val appCompatVersion: String by rootProject.extra
    val constraintLayoutVersion: String by rootProject.extra
    val kotlinUtilsVersion: String by rootProject.extra
    val lifecycleVersion: String by rootProject.extra
    val materialVersion: String by rootProject.extra
    val navigationVersion: String by rootProject.extra

    val androidxJunitVersion: String by rootProject.extra
    val androidxTestVersion: String by rootProject.extra
    val espressoVersion: String by rootProject.extra
    val junitVersion: String by rootProject.extra

    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("xyz.lbres:kotlin-utils:$kotlinUtilsVersion")

    implementation(project(":exactnumbers"))

    // testing
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    androidTestImplementation("androidx.test:rules:$androidxTestVersion")
    androidTestImplementation("androidx.test:runner:$androidxTestVersion") // needed to run on emulator
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-intents:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
}
