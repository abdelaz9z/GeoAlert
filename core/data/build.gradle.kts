plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.google.dagger.hilt.android)
    id("kotlinx-serialization")
}

android {
    namespace = "com.casecode.core.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.network)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // kotlinx
    implementation(libs.androidx.tracing.ktx)

    // =============================================================================================
    // firebase
    // =============================================================================================
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.performance)
    implementation (libs.firebase.database.ktx)
    // ==============================================

    // =============================================================================================
    // credentials
    // =============================================================================================
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    // ==============================================

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(projects.core.testing)
}

kapt {
    correctErrorTypes = true
}