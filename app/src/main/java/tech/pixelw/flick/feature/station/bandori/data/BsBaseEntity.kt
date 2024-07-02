package tech.pixelw.flick.feature.station.bandori.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
open class BsBaseEntity(
    @Json(name = "timestamp")
    var timestamp: Long = 0,
    @Json(name = "user_info")
    var userInfo: BsUserInfo = BsUserInfo()

) {
    fun isMySide(myId: String): Boolean {
        return userInfo.userId == myId
    }
}