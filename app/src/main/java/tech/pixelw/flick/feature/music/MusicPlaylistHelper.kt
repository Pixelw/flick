package tech.pixelw.flick.feature.music

import androidx.media3.common.MediaItem
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.feature.music.data.MusicModel
import java.util.concurrent.CopyOnWriteArraySet

object MusicPlaylistHelper {

    private var playList = mutableListOf<MusicModel>()

    private val mediaIdItemMap = hashMapOf<String, MediaItem>()

    private val listenerList = CopyOnWriteArraySet<Listener>()

    var playIndex = -1
        set(value) {
            field = value
            notifyListenersIndexChanged(value)
        }

    private fun notifyListenersIndexChanged(value: Int) {
        val model = getCurrentMusicModel()
        for (listener in listenerList) {
            listener.onPlayIndexChanged(value, model)
        }
    }

    fun getPlaylist(): List<MusicModel> {
        return playList
    }

    fun setPlaylist(list: List<MusicModel>) {
        playList = ArrayList(list)
        playIndex = -1
    }

    fun addListener(listener: Listener) {
        listenerList.add(listener)
    }

    fun removeListener(listener: Listener) {
        listenerList.remove(listener)
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
        return selectMusicById(mediaId)?.cachedOrConvertMediaItem()
    }

    fun selectMusicById(mediaId: String): MusicModel? {
        for (i in playList.indices) {
            val model = playList[i]
            if (model.mediaId == mediaId) {
                playIndex = i
                return model
            }
        }
        LogUtil.w("mediaId: $mediaId not found", TAG)
        return null
    }

    fun getCurrentMusicModel(): MusicModel? {
        if (playList.isEmpty() || playIndex < 0) return null
        return playList[playIndex]
    }

    fun getCurrentMediaItem(): MediaItem? {
        return getCurrentMusicModel()?.cachedOrConvertMediaItem()
    }

    fun getMediaItemList(): List<MediaItem> {
        return playList.map { it.cachedOrConvertMediaItem() }
    }


    /**
     * 用于不支持多item的播放器
     */
    fun next(): MediaItem? {
        if (playList.isEmpty()) return null
        if (playIndex in -1 until playList.lastIndex) {
            playIndex++
        } else {
            playIndex = 0
        }
        return playList.getOrNull(playIndex)?.cachedOrConvertMediaItem()
    }

    /**
     * 用于不支持多item的播放器
     */
    fun previous(): MediaItem? {
        if (playList.isEmpty()) return null
        if (playIndex in 1..playList.lastIndex) {
            playIndex--
        } else {
            playIndex = 0
        }
        return playList.getOrNull(playIndex)?.cachedOrConvertMediaItem()
    }

    /**
     * 转换为MediaItem并缓存, 避免重复生成
     */
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

    interface Listener {

        fun onPlayIndexChanged(playIndex: Int, musicModel: MusicModel?) {}

    }

}