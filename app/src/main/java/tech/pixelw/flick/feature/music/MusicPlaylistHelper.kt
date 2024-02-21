package tech.pixelw.flick.feature.music

import androidx.media3.common.MediaItem
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.feature.music.data.MusicModel

object MusicPlaylistHelper {

    private var playList = mutableListOf<MusicModel>()

    private val mediaIdItemMap = hashMapOf<String, MediaItem>()

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
        mediaIdItemMap.clear()
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
                return model.cachedOrConvertMediaItem()
            }
        }
        LogUtil.w("mediaId: $mediaId not found", TAG)
        return null
    }

    fun getMediaItemList(): List<MediaItem> {
        return playList.map { it.cachedOrConvertMediaItem() }
    }

    fun next(): MediaItem? {
        if (playList.isEmpty()) return null
        if (playIndex in -1 until playList.lastIndex) {
            playIndex++
        } else {
            playIndex = 0
        }
        return playList.getOrNull(playIndex)?.cachedOrConvertMediaItem()
    }

    fun previous(): MediaItem? {
        if (playList.isEmpty()) return null
        if (playIndex in 1..playList.lastIndex) {
            playIndex--
        } else {
            playIndex = 0
        }
        return playList.getOrNull(playIndex)?.cachedOrConvertMediaItem()
    }

    private fun MusicModel.cachedOrConvertMediaItem(): MediaItem {
        val item = mediaIdItemMap[mediaId]
        return if (item != null) {
            item
        } else {
            val item1 = toMediaItem()
            mediaIdItemMap[mediaId] = item1
            item1
        }
    }

    private const val TAG = "MusicPlaylistHelper"
}