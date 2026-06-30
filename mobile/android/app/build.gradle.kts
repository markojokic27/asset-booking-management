import java.util.Properties

// Load local.properties at the top of the file
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) load(f.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    id("jacoco")

}

android {
    namespace = "com.example.assetbookingmanagement"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.assetbookingmanagement"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            buildConfigField(
                "String", "BASE_URL",
                "\"${localProps.getProperty("BASE_URL", "http://192.168.211.12:8080/v1/")}\"" 
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField(
                "String", "BASE_URL",
                "\"${localProps.getProperty("BASE_URL", "https://your-prod-domain.com/v1/")}\"" 
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true 
    }
    lint {
        xmlReport = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.play.services.code.scanner)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}



jacoco {
    toolVersion = "0.8.14"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(
            layout.buildDirectory.file(
                "reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
            )
        )
        html.outputLocation.set(
            layout.buildDirectory.dir(
                "reports/jacoco/jacocoTestReport/html"
            )
        )
    }

    val fileFilter = listOf(
    "**/R.class", "**/R\$*.class", "**/BuildConfig.*",
    "**/Manifest*.*", "**/*Test*.*", "android/**/*.*",
    "**/Hilt_*.*",
    "**/*_Factory.*",
    "**/*_Provide*Factory.*",       // NetworkModule_ProvideXFactory
    "**/*_MembersInjector*.*",
    "**/*_GeneratedInjector.*",
    "**/*_HiltModules*.*",
    "**/*_HiltComponents*.*",
    "**/*_ComponentTreeDeps*.*",
    "**/Dagger*.*",
    "**/dagger/**",
    "**/hilt_aggregated_deps/**",
    "**/codegen/**"
)

    val debugTree = fileTree(
        layout.buildDirectory.dir(
            "intermediates/classes/debug/transformDebugClassesWithAsm/dirs"
        )
    ) {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        layout.buildDirectory.file(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
        )
    )
}

tasks.withType<Test>().configureEach {
    if (name == "testDebugUnitTest") {
        ignoreFailures = false
    }
}
sonar {
    properties {
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            layout.buildDirectory.file("reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
                .get().asFile.path
        )
        property(
            "sonar.androidLint.reportPaths",
            layout.buildDirectory.file("reports/lint-results-debug.xml")
                .get().asFile.path
        )
    }
}
