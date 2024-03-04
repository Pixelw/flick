package tech.pixelw.flick.core.extension

import tech.pixelw.flick.core.network.moshi

inline fun <reified T> String.toObject(): T? {
    return moshi.adapter(T::class.java).fromJson(this)
}

inline fun <reified T> T.toJson(): String? {
    return moshi.adapter(T::class.java).toJson(this)
}

