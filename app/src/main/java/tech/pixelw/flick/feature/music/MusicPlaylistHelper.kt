package tech.pixelw.flick.feature.music

import android.media.browse.MediaBrowser.MediaItem
import tech.pixelw.flick.feature.music.data.MusicModel

object MusicPlaylistHelper {


    private var playList = mutableListOf<MusicModel>()

    var playIndex = -1
        private set

    fun getPlaylist(): List<MusicModel> {
        return playList
    }

    fun setPlaylist(list: List<MusicModel>) {
        playList = ArrayList(list)
        playIndex = -1
    }

    fun next(): MediaItem {

    }

    fun previous(): MediaItem {

    }

}