// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.10"
    id ("com.google.dagger.hilt.android") version "2.48" apply false
}

buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}
/*
tasks.withType(org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask::class.java).configureEach {
   kotlinOptions.jvmTarget = "1.8"
}
*/
