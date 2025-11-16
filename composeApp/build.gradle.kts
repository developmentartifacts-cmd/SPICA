// Full updated build.gradle.kts for the shared KMP module

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.googleFirebaseCrashlytics)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        
        // --- ANDROID SPECIFIC DEPENDENCIES (KEEPING ALL PLATFORM LIBRARIES) ---
        androidMain.dependencies {
            implementation("androidx.media3:media3-exoplayer:1.4.1")
            implementation("androidx.media3:media3-ui:1.4.1")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
            implementation("androidx.core:core-splashscreen:1.0.1")
            
            // KMP Architectural Fixes
            implementation(libs.moko.mvvm.compose) // Android-specific KMP ViewModel integration
            implementation(libs.koin.compose) // Android-specific Koin integration

            // Compose Android Libraries
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        
        // --- COMMON MODULE FOUNDATION (THE CRITICAL FIXES) ---
        commonMain.dependencies {
            // Data and Serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            
            // KMP Architectural Foundation
            implementation(libs.kotlinx.coroutines.core) // 💡 CRITICAL: Coroutines core for all suspend functions
            implementation(libs.koin.core)               // 💡 CRITICAL: Dependency Injection core
            implementation(libs.moko.mvvm)               // 💡 CRITICAL: KMP-safe ViewModel base (replace AndroidX ViewModel)

            // Compose Libraries
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            // Note: Removed redundant AndroidX lifecycle dependencies
            // implementation(libs.androidx.lifecycle.viewmodelCompose)
            // implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
        }
        
        // --- TEST DEPENDENCIES (Adding KMP Test Tools) ---
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test) // 💡 For testing suspend functions
            implementation(libs.koin.test)               // 💡 For testing DI modules
        }
        
        // --- JVM/Desktop SPECIFIC DEPENDENCIES ---
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.gibson.spica"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.gibson.spica"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Keeping all Firebase/Android dependencies in the dependencies block (implicitly for the Android application)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.firebase.config)
    implementation(libs.play.services.ads)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.gibson.spica.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.gibson.spica"
            packageVersion = "1.0.0"
        }
    }
}
