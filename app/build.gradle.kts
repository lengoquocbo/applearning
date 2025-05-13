plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")  // Đảm bảo KAPT được sử dụng cho Hilt
    id("com.google.devtools.ksp")  // KSP vẫn được sử dụng cho Room
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.1.0"
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.apphoctap"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.apphoctap"
        minSdk = 24
        targetSdk = 34
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", "true")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.runtime)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.serialization.json)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.volley)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    ksp(libs.androidx.room.compiler)


    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)  // Sử dụng KAPT cho Hilt

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.circleimageview)
    implementation(libs.androidx.viewpager2)


    implementation(libs.glide) // Thêm Glide vào dependencies


    // UI Components từ Stream Chat SDK
    implementation (libs.stream.chat.android.client)
    implementation (libs.stream.chat.android.state)
    implementation (libs.stream.chat.android.offline)
    implementation (libs.stream.chat.android.compose)

    // Stream Video SDK - Core client
    implementation(libs.getstream.stream.video.android.core)


    implementation(libs.shimmer)
    implementation(libs.lottie)



}

