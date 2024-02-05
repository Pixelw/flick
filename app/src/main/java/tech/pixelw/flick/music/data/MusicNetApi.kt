package tech.pixelw.flick.music.data

import retrofit2.http.GET

interface MusicNetApi {

    @GET
    suspend fun getAllMusic(): MusicAllListResponse

}