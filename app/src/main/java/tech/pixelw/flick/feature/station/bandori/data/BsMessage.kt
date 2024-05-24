package tech.pixelw.flick.feature.station.bandori.data


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class BsMessage(
    @Json(name = "content")
    val content: String = ""
) : BsBaseEntity()