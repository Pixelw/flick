package tech.pixelw.flick.music.data

import retrofit2.http.GET

interface MusicNetApi {

    @GET("all_music.json")
    suspend fun getAllMusic(): List<MusicModel>

}