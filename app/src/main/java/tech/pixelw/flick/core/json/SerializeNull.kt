package tech.pixelw.flick.core.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

/**
 * 标记需要写出 JSON null 的字段；Kotlin 注解默认使用运行时保留策略。
 * 不重复声明 Retention，避免 Moshi 1.x 在 KSP2 下解析保留策略枚举时崩溃。
 * @author HBB20@StackOverflow
 */
@JsonQualifier
annotation class SerializeNull {
    companion object {
        object Factory : JsonAdapter.Factory {
            override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                val nextAnnotations = Types.nextAnnotations(annotations, SerializeNull::class.java)

                return if (nextAnnotations == null) {
                    null
                } else {
                    NullIfNullJsonAdapter<Any>(moshi.nextAdapter(this, type, nextAnnotations))
                }
            }
        }
    }
}
