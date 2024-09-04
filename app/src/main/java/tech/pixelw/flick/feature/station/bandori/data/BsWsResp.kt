package tech.pixelw.flick.feature.station.bandori.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


//{"status":"success","action":"sendServerTime","response":{"time":1725415891610}}

@Keep
@JsonClass(generateAdapter = true)
class BsWsResp<T>(
    @Json(name = "status") val status: String,
    @Json(name = "action") val action: String,
    @Json(name = "response") val response: T
) {
    fun isSuccess() = status == "success"
}