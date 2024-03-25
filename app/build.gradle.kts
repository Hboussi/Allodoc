plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.allodoc"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.allodoc"
        minSdk = 25
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation ("com.square.retrofit2:retrofit:2.9.0")
//    implementation ("com.square.retrofit2:converter-gson:2.9.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation("androidx.core:core-ktx:+")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.annotation:annotation:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp (optionnel, mais souvent utilisé avec Retrofit)
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // CARD VIEW
    implementation ("androidx.cardview:cardview:1.0.0")

    //Picasso
    implementation ("com.squareup.picasso:picasso:2.71828")


    // COMMONS
    implementation ("commons-io:commons-io:2.11.0")

    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.2.0")


    // ZXING
    implementation ("com.google.zxing:core:3.3.3")


}