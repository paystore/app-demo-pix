import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.phoebus.pix.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.phoebus.pix.demo"
        minSdk = 22
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            android.buildFeatures.buildConfig = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            android.buildFeatures.buildConfig = true
        }

        applicationVariants.all {
            val variant = this
            variant.outputs.all {
                val output = this as BaseVariantOutputImpl
                output.outputFileName = "${rootProject.name}-${variant.versionName}-${variant.name}.apk"
            }
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val navigationVersion = "2.7.7"
val material3Version = "1.2.1"
val junitVersion = "4.13.2"
val activityVersion = "1.9.0"
val lifecycleVersion = "2.7.0"
val coreVersion = "1.13.0"
val gsonVersion = "2.10.1"
val pixSdkVersion = "1.0.1.0"

dependencies {

    implementation("com.phoebus.libraries:pix-sdk:$pixSdkVersion")
    //implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar")))) // faz referencia ao .aar local
    implementation("androidx.core:core-ktx:$coreVersion")
    implementation("androidx.activity:activity-compose:$activityVersion")

    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    implementation("androidx.navigation:navigation-compose:$navigationVersion")

    implementation("com.google.code.gson:gson:$gsonVersion")

    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}

