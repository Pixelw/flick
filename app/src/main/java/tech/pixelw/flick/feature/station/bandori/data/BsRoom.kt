package tech.pixelw.flick.feature.station.bandori.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BsRoom(
    @Json(name = "number")
    val number: String = "",
    @Json(name = "raw_message")
    var message: String = "",
    @Json(name = "type")
    val type: String = "",
    @Json(name = "source_info")
    val sourceInfo: SourceInfo = SourceInfo()
) : BsBaseEntity() {

    @JsonClass(generateAdapter = true)
    class SourceInfo(
        @Json(name = "name")
        val name: String = "",
        @Json(name = "type")
        val type: String = ""
    )

}