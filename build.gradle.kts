// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false

/*
    // Make sure that you have the Google services Gradle plugin dependency
    id("com.google.gms:google-services") version ("4.3.15") apply false
    // Add the dependency for the Crashlytics Gradle plugin
    id("com.google.firebase:firebase-crashlytics-gradle") version ("2.9.5") apply false
    */
    id("com.google.dagger.hilt.android") version ("2.51.1") apply false
}