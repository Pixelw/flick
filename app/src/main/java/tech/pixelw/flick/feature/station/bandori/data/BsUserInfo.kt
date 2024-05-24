package tech.pixelw.flick.feature.station.bandori.data


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class BsUserInfo(
    @Json(name = "avatar")
    val avatar: String = "",
    @Json(name = "user_id")
    val userId: String = "",
    @Json(name = "username")
    val username: String = "",
    @Json(name = "type")
    val type: String = "",
    @Json(name = "role")
    val role: Int = 0
) {
    fun getAvatarUrl(): String {
        return if (type.contains("qq", ignoreCase = true)) {
            "https://q1.qlogo.cn/g?b=qq&nk=$userId&s=640"
        } else {
            "https://asset.bandoristation.com/images/user-avatar/$avatar"
        }
    }
}