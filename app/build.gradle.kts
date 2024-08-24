import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

var properties: Properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.project.veganlife"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.project.veganlife"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["KAKAO_API_KEY"] = properties.getProperty("TEST_KAKAO_NATIVE_KEY")

        buildConfigField("String", "KAKAO_API_KEY", properties.getProperty("TEST_KAKAO_API_KEY"))
        buildConfigField("String", "NAVER_CLIENT_ID", properties.getProperty("NAVER_CLIENT_ID"))
        buildConfigField("String", "NAVER_CLIENT_SECRET_KEY", properties.getProperty("NAVER_CLIENT_SECRET_KEY"))
        buildConfigField("String", "AWS_S3_ACCESSKEY_", properties.getProperty("AWS_S3_ACCESSKEY_"))
        buildConfigField("String", "AWS_S3_SECRET_ACCESSKEY", properties.getProperty("AWS_S3_SECRET_ACCESSKEY"))
        buildConfigField("String", "VEGAN_LIFE_CDN_ADDRESS", properties.getProperty("VEGAN_LIFE_CDN_ADDRESS"))
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
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp3
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.46")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

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
    implementation("com.kakao.sdk:v2-user:2.19.0")

    // Naver Login
    implementation("com.navercorp.nid:oauth:5.1.0") // jdk 11

    // Expandable Layout
    implementation("com.github.skydoves:expandablelayout:1.0.7")

    // Dots Indicator
    implementation("com.tbuonomo:dotsindicator:5.0")

    // EncryptedSharedPreference
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    // Circle Imageview
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // Paging 3
    implementation ("androidx.paging:paging-runtime:3.3.0")

    // Preferences Datastore
    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    // GSON
    implementation("com.google.code.gson:gson:2.11.0")

    // Aws
    implementation ("com.amazonaws:aws-android-sdk-s3:2.22.5")
    implementation ("com.amazonaws:aws-android-sdk-mobile-client:2.22.0")
}