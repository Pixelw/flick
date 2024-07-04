package tech.pixelw.flick.feature.station

import retrofit2.http.GET
import retrofit2.http.Query
import tech.pixelw.flick.feature.station.bandori.data.BsApiResp
import tech.pixelw.flick.feature.station.bandori.data.BsOnlineNumber
import tech.pixelw.flick.feature.station.bandori.data.BsRoom

interface BsWebApi {

    @GET("?function=get_online_number")
    suspend fun getOnlineNumber(): BsApiResp<BsOnlineNumber>

    @GET("?function=query_room_number")
    suspend fun getRooms(): BsApiResp<List<BsRoom>>

    @GET("?function=get_user_info")
    suspend fun getUserInfo(@Query("user_id") bsId: Long)

}
