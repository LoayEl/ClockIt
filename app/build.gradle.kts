plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.clockitproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.clockitproject"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // ─── Java 8 + core-library desugaring ────────────────────────────
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled  = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // ────────────────────────────────────────────────────────────────

    buildFeatures {
        compose = true
    }
    composeOptions {
        // pull from your version catalog — this must match your Compose BOM
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
    // ─── desugaring library ─────────────────────────────────────────
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.3")

    // ─── AndroidX + Compose core ────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ─── Compose BOM & UI libraries ────────────────────────────────
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)                  // compose-ui
    implementation(libs.androidx.ui.graphics)         // compose-ui-graphics
    implementation(libs.androidx.ui.tooling.preview) // tooling-preview
    implementation(libs.androidx.material3)

    // ─── ViewModel + Compose bridge ───────────────────────────────
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // ─── Material icons (extended) ─────────────────────────────────
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // ─── Testing & debug ────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.navigation:navigation-compose:2.9.0")

}
