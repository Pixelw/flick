package tech.pixelw.flick.feature.station.bandori.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class BsApiResp<T>(
    @Json(name = "status") val status: String,
    @Json(name = "response") val response: T?
) {
    fun isSuccess() = status == "success"
}