package tech.pixelw.flick.core.extension

import com.squareup.moshi.adapter
import tech.pixelw.flick.core.json.moshi

/** 将 JSON 字符串解析为指定类型，保留集合及嵌套类型的泛型信息。 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> String.toObject(): T? {
    return moshi.adapter<T>().fromJson(this)
}

/** 按指定类型及其泛型信息序列化对象，返回 JSON 字符串。 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> T.toJson(): String {
    return moshi.adapter<T>().toJson(this)
}

