package tech.pixelw.flick.core.extension

import android.content.Intent

fun Intent.stringExtra(key: String): String? {
    return runCatching {
        this.getStringExtra(key)
    }.getOrNull()
}

fun Intent.intExtra(key: String): Int? {
    return runCatching {
        this.getIntExtra(key, 0)
    }.getOrNull()
}