plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0" // ktlint
}

android {
    namespace = "xyz.lbres.exactnumbers"
    compileSdk = 33

    defaultConfig {
        // minSdk = 29
        minSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    sourceSets.getByName("main") {
        java.setSrcDirs(listOf("src/main/kotlin"))
    }

    sourceSets.getByName("test") {
        java.setSrcDirs(listOf("src/test/kotlin"))
    }

    sourceSets.getByName("androidTest") {
        kotlin.setSrcDirs(listOf("src/espresso/kotlin"))
        java.setSrcDirs(listOf("src/espresso/kotlin"))
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    val androidxCoreVersion: String by rootProject.extra
    val kotlinUtilsVersion: String by rootProject.extra

    val androidxJunitVersion: String by rootProject.extra
    val espressoVersion: String by rootProject.extra
    val junitVersion: String by rootProject.extra
    val mockkVersion: String by rootProject.extra

    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    implementation("xyz.lbres:kotlin-utils:$kotlinUtilsVersion")

    testImplementation(kotlin("test"))

    testImplementation("io.mockk:mockk-android:$mockkVersion")
    testImplementation("io.mockk:mockk-agent:$mockkVersion")
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
}
