package tech.pixelw.flick.feature.station.bandori.data


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class BsWsTime(
    @Json(name = "time")
    val time: Long = 0
)