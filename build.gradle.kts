// Top-level build file where you can add configuration options common to all sub-projects/modules.
// 显式更新 AGP 内置 Kotlin 的底层编译器，以支持新库的 Kotlin 元数据。
buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.21")
    }
}

plugins {
    id("com.android.application") version "9.4.1" apply false
    id("com.android.legacy-kapt") version "9.4.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.21" apply false
    id("com.google.devtools.ksp") version "2.3.12" apply false
}
