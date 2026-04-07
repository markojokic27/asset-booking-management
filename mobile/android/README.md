# Android Setup and Running

This project uses **Kotlin + Jetpack Compose + Gradle**.

---

Before running the project, make sure you have installed:

- **Android Studio**
- **JDK 17 or newer**
- **Android SDK**

Check Java version:

```bash
java -version
```

---

## Installation

Navigate to the Android directory:

```bash
cd mobile/android
```

Open the project in **Android Studio** or use the Gradle wrapper from terminal.

---

## Key Dependencies

The project uses the following main tools and libraries:

- Kotlin
- Jetpack Compose
- Android Gradle Plugin
- Material 3
- JUnit

Dependencies are resolved automatically by Gradle.

---

## Run the Application

To run the app from Android Studio:

- open `mobile/android`
- sync Gradle
- start an emulator or connect a device
- run the `app` configuration

To build from terminal:

```bash
cd mobile/android
./gradlew assembleDebug
```
