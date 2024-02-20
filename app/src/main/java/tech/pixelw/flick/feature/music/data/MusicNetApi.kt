package tech.pixelw.flick.feature.music.data

import retrofit2.http.GET

interface MusicNetApi {

    @GET("all_music.json")
    suspend fun getAllBandoriMusic(): List<MusicModel>

}