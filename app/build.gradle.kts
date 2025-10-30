import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin") // Safe Args 활성화
}

var properties: Properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.project.veganlife"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.project.veganlife"
        minSdk = 26
        targetSdk = 35
        versionCode = 9
        versionName = "1.1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["KAKAO_API_KEY"] = properties.getProperty("KAKAO_NATIVE_KEY")

        buildConfigField("String", "KAKAO_API_KEY", properties.getProperty("KAKAO_API_KEY"))
        buildConfigField("String", "NAVER_CLIENT_ID", properties.getProperty("NAVER_CLIENT_ID"))
        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET_KEY",
            properties.getProperty("NAVER_CLIENT_SECRET_KEY")
        )
        buildConfigField("String", "BASEURL", properties.getProperty("BASE_URL"))

        ndk {
            // ✅ Android 15 16KB page 지원
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        // ✅ 16KB 페이지 정렬 반영
        packaging {
            jniLibs {
                useLegacyPackaging = false
            }
        }
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
    kapt {
        correctErrorTypes = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")

    // UI Test
    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test:runner:1.6.2")



    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp3
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.okhttp3:okhttp-sse:4.9.3")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:2.8.5")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")

    // Splash
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // AnyChart
    implementation("com.github.AnyChart:AnyChart-Android:1.1.5")

    // Kakao Login
    implementation("com.kakao.sdk:v2-user:2.21.2")

    // Naver Login
    implementation("com.navercorp.nid:oauth:5.1.1") // jdk 11

    // Expandable Layout
    implementation("com.github.skydoves:expandablelayout:1.0.7")

    // Dots Indicator
    implementation("com.tbuonomo:dotsindicator:5.0")

    // EncryptedSharedPreference
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Circle Imageview
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Paging 3
    implementation("androidx.paging:paging-runtime:3.3.0")

    // Preferences Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // GSON
    implementation("com.google.code.gson:gson:2.11.0")

    //flexbox layout
    implementation("com.google.android.flexbox:flexbox:3.0.0")
}