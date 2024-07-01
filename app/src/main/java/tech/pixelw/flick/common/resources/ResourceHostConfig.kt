package tech.pixelw.flick.common.resources

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class HostConfig(
    @Json(name = "default")
    val default: String = "",
    @Json(name = "hostList")
    val hostList: List<Host> = listOf()
) {
    fun getDefaultHost(): Host? = hostList.find { it.name == default }
}

@Keep
@JsonClass(generateAdapter = true)
data class Host(
    @Json(name = "baseUrl")
    val baseUrl: String = "",
    @Json(name = "name")
    val name: String = ""
)