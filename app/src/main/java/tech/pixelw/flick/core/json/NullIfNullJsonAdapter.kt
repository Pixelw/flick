package tech.pixelw.flick.core.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

/**
 * @author HBB20@StackOverflow
 */
class NullIfNullJsonAdapter<T>(private val delegate: JsonAdapter<T>) : JsonAdapter<T>() {

    override fun fromJson(reader: JsonReader): T? {
        return delegate.fromJson(reader)
    }

    override fun toJson(writer: JsonWriter, value: T?) {
        if (value == null) {
            val serializeNulls: Boolean = writer.serializeNulls
            writer.serializeNulls = true
            try {
                delegate.toJson(writer, value)
            } finally {
                writer.serializeNulls = serializeNulls
            }
        } else {
            delegate.toJson(writer, value)
        }
    }

    override fun toString(): String {
        return "$delegate.serializeNulls()"
    }
}