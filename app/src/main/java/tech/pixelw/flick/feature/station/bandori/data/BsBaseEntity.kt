package tech.pixelw.flick.feature.station.bandori.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
open class BsBaseEntity(
    @Json(name = "timestamp")
    val timestamp: Long = 0,
    @Json(name = "user_info")
    val userInfo: BsUserInfo = BsUserInfo()

) {
    fun isMySide(myId: String): Boolean {
        return userInfo.userId == myId
    }
}