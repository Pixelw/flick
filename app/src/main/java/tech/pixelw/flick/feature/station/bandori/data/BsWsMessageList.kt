package tech.pixelw.flick.feature.station.bandori.data


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class BsWsMessageList(
    @Json(name = "is_end")
    val isEnd: Boolean = false,
    @Json(name = "message_list")
    val messageList: List<BsMessage> = emptyList(),
    @Json(name = "self_id")
    val selfId: String = ""
)