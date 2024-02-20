package tech.pixelw.flick.feature.music.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tech.pixelw.flick.common.resources.ResourceConfig
import tech.pixelw.flick.common.resources.Urls

@JsonClass(generateAdapter = true)
data class MusicModel(
    @Json(name = "arranger")
    val arranger: String? = null,
    @Json(name = "bandId")
    val bandId: Int = 0,
    @Json(name = "bandName")
    val bandName: String? = null,
    @Json(name = "bgmId")
    val bgmId: String = "",
    @Json(name = "composer")
    val composer: String? = null,
    @Json(name = "description")
    val description: List<String>? = null,
    @Json(name = "jacketImage")
    val jacketImage: List<String> = listOf(),
    @Json(name = "length")
    val length: Double = 0.0,
    @Json(name = "lyricist")
    val lyricist: String? = null,
    @Json(name = "musicSerialId")
    val musicSerialId: Int = 0,
    @Json(name = "musicTitle")
    val musicTitle: String? = null,
    @Json(name = "publishedAt")
    val publishedAt: List<Long> = listOf()
) {

    val mediaId = "bandoriBgm$musicSerialId"

    fun getSongArtUrl(): String {
        return when (ResourceConfig.musicArtImg) {
            ResourceConfig.LAPI -> "${Urls.lapiRootPath}img/musicjacket/${musicSerialId}_jacket1.webp"
            else -> ""
        }

    }

    fun getSongFileUrl(): String {
        return when (ResourceConfig.musicFile) {
            ResourceConfig.LAPI -> "${Urls.lapiRootPath}music/${musicSerialId}.mp3"
            else -> ""
        }
    }


}