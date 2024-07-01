package tech.pixelw.flick.feature.music.data

import tech.pixelw.flick.common.getApis
import tech.pixelw.flick.core.misc.LogUtil

object MusicListRepository {

    private const val TAG = "MusicListRepository"

    const val BANDORI_DEFAULT_PLAY_ROOT = "bandori_default_root_id"
    const val PRSK_DEFAULT_PLAY_ROOT = "prsk_default_root_id"


    private var cachedListMap: MutableMap<String, List<MusicModel>> = mutableMapOf()
//    private var cachedMetadataListMap: MutableMap<String, List<MediaMetadata>> = mutableMapOf()
//    private var cachedMediaItemListMap: MutableMap<String, List<MediaItem>> = mutableMapOf()

    suspend fun getMusicList(rootId: String): List<MusicModel> {
        val cached = cachedListMap[rootId]
        if (cached != null) return cached
        when (rootId) {
            BANDORI_DEFAULT_PLAY_ROOT -> {
                getApis(MusicNetApi::class.java)?.runCatching {
                    getAllBandoriMusic()
                }?.onSuccess {
                    cachedListMap[rootId] = it
                    return it
                }?.onFailure {
                    LogUtil.e("get music list failed", it, tag = TAG)
                }
            }

            PRSK_DEFAULT_PLAY_ROOT -> throw RuntimeException("not implemented yet")
            else -> return emptyList()
        }
        return emptyList()
    }

}