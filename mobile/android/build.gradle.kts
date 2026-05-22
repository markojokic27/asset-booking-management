// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
     id("org.sonarqube") version "7.3.0.8198"
}


sonar {
    properties {
        property("sonar.projectKey",     "asset-booking-management-android")
        property("sonar.projectName",    "AssetBookingManagement Android")
        property("sonar.androidLint.reportPaths",
            "${project.projectDir}/app/build/reports/lint-results-debug.xml")
        property("sonar.coverage.jacoco.xmlReportPaths",
            "${project.projectDir}/app/build/reports/coverage/test/debug/report.xml")
    }
}