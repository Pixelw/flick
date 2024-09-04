package tech.pixelw.flick.core.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
/**
 * @author HBB20@StackOverflow
 */
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