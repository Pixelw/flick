package tech.pixelw.flick.music.data

import tech.pixelw.flick.common.network.NetApiFactory

class MusicListRepository {

    private val api by lazy {
        NetApiFactory.get(MusicNetApi::class.java)
    }

    suspend fun getMusicList(): List<MusicModel> {
        return api.getAllMusic()
    }

}