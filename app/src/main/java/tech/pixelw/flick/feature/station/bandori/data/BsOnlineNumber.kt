package tech.pixelw.flick.feature.station.bandori.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BsOnlineNumber(@Json(name = "online_number") val onlineNumber: Int = 0)