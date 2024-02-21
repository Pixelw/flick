package tech.pixelw.flick.feature.music

import androidx.media3.common.MediaItem
import tech.pixelw.flick.core.misc.LogUtil
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

    fun clear() {
        playList.clear()
        playIndex = -1
    }

    fun shuffle() {
        if (playList.isEmpty()) return
        val currentMusic = if (playIndex >= 0) {
            playList[playIndex]
        } else null
        playList.shuffle()
        // 随机后播放位置变更
        if (currentMusic != null) {
            playIndex = playList.indexOf(currentMusic)
        }
    }

    fun reverse() {
        if (playList.isEmpty()) return
        val currentMusic = if (playIndex >= 0) {
            playList[playIndex]
        } else null
        playList.reverse()
        if (currentMusic != null) {
            playIndex = playList.indexOf(currentMusic)
        }
    }

    fun selectMediaItemById(mediaId: String): MediaItem? {
        for (i in playList.indices) {
            val model = playList[i]
            if (model.mediaId == mediaId) {
                playIndex = i
                return model.toMediaItem()
            }
        }
        LogUtil.w("mediaId: $mediaId not found", TAG)
        return null
    }

    fun next(): MediaItem? {
        if (playList.isEmpty()) return null
        if (playIndex in -1 until playList.lastIndex) {
            playIndex++
        } else {
            playIndex = 0
        }
        return playList.getOrNull(playIndex)?.toMediaItem()
    }

    fun previous(): MediaItem? {
        if (playList.isEmpty()) return null
        if (playIndex in 1..playList.lastIndex) {
            playIndex--
        } else {
            playIndex = 0
        }
        return playList.getOrNull(playIndex)?.toMediaItem()
    }

    private const val TAG = "MusicPlaylistHelper"
}