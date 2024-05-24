package tech.pixelw.flick.feature.station.bandori.data

import com.squareup.moshi.Json

class BsWsResponse<RespModel : Any>(
    @Json(name = "status") status: String,
    @Json(name = "action") action: String,
    @Json(name = "response") response: RespModel? = null
)