package tech.pixelw.flick.feature.music

import tech.pixelw.flick.common.resources.ResourceConfig
import tech.pixelw.flick.feature.music.data.MusicModel

object MusicResMapper {

    @JvmStatic
    fun getSongArtUrl(musicModel: MusicModel): String {
        return when (ResourceConfig.musicArtImg) {
            ResourceConfig.LAPI -> "https://lapi.pixelw.tech/bandroid/img/musicjacket/${musicModel.musicSerialId}_jacket1.webp"
            else -> ""
        }

    }

    @JvmStatic
    fun getSongFileUrl(musicModel: MusicModel): String {
        return when (ResourceConfig.musicFile) {
            ResourceConfig.LAPI -> "https://lapi.pixelw.tech/bandroid/music/${musicModel.musicSerialId}.mp3"
            else -> ""
        }
    }

}