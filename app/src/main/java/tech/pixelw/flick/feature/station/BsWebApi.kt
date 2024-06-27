package tech.pixelw.flick.feature.station

import retrofit2.http.GET
import retrofit2.http.Query
import tech.pixelw.flick.feature.station.bandori.data.BsRoom

interface BsWebApi {

    @GET("?function=get_online_number")
    suspend fun getOnlineNumber(): Int

    @GET("?function=query_room_number")
    suspend fun getRooms(): List<BsRoom>

    @GET("/")
    suspend fun getQqWithId(@Query("function") func: String, @Query("user_id") bsId: Long)

}
