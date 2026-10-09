# Repository Guidelines

## Project Structure & Module Organization

Flick is a single-module Android application (`:app`) using Kotlin, Jetpack Compose, fragments, and XML layouts. Sources live in `app/src/main/java/tech/pixelw/flick/`:

- `feature/home`, `feature/music`, and `feature/station`: screens, view models, repositories, and playback/station behavior.
- `core/`: shared media, networking, JSON, UI, and extension utilities.
- `common/resources/`: resource-host configuration and mapping; `theme/`: Compose styling.

Layouts, navigation graphs, strings, and drawable assets belong in `app/src/main/res/`. JVM tests live in `app/src/test/`; device tests live in `app/src/androidTest/`.

## Build, Test, and Development Commands

Use the checked-in Gradle wrapper with `--no-daemon`. The project uses Gradle 8.7, Android Gradle Plugin 8.4.2, and compile SDK 34; use JDK 17 and an installed Android SDK.

- `./gradlew :app:assembleDebug --no-daemon`: build the default development APK.
- `./gradlew :app:testDebugUnitTest --no-daemon`: run JVM tests.
- `./gradlew :app:connectedDebugAndroidTest --no-daemon`: run instrumentation tests on a connected device.
- `./gradlew :app:lintDebug --no-daemon`: run Android Lint.
- `android run --apks app/build/outputs/apk/debug/app-debug.apk`: deploy and launch for device verification; do not add `--debug`.

For requested release builds, use `./gradlew :app:assembleRelease --no-daemon --no-parallel --max-workers=1 -Pkotlin.parallel.tasks.in.project=false`. Release currently uses debug signing; configure production signing before distribution.

## Coding Style & Naming Conventions

Follow official Kotlin style with four-space indentation and Android Studio formatting. Use `PascalCase` for classes and composables, `camelCase` for functions/properties, and `snake_case` for resource names. Match existing suffixes such as `ViewModel`, `Repository`, and `Fragment`. No dedicated ktlint or Detekt configuration exists.

Document every newly written public method, function, and property with a Chinese comment explaining its actual behavior.

## Testing Guidelines

The project provides JUnit 4, AndroidX JUnit, Espresso, and Compose UI testing dependencies. Existing tests are starter examples; no coverage threshold is configured. Name test classes `<Subject>Test` and methods descriptively. Verify affected navigation, playback, and station behavior on a device. Per owner instructions, delete unit-test files created for the task after they pass.

## Commit & Pull Request Guidelines

Recent commits use short action descriptions, mostly Chinese, without a mandatory prefix. Keep commits focused. PRs should describe behavior changes, link relevant issues, report validation, and include screenshots for visible UI changes.

## Agent & Configuration Instructions

After application changes, run on a device for user verification. Prefer the android-cli skill for deployment, screenshots, layout inspection, and documentation. Automate user input only when authorized. Ask before installing tools. Keep SDK paths in ignored `local.properties`; never commit credentials or signing secrets.
