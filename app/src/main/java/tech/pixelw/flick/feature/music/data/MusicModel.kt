package tech.pixelw.flick.feature.music.data


import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tech.pixelw.flick.common.resources.ResourceConfig
import tech.pixelw.flick.common.resources.ResourceMapper
import tech.pixelw.flick.common.resources.Urls
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

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

    private fun getFirstPublish(): Long {
        return publishedAt.minByOrNull {
            if (it <= 0) {
                Long.MAX_VALUE
            } else it
        } ?: -1
    }

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

    fun toMediaItem(): MediaItem {
        val calendar = Calendar.Builder()
            .setInstant(getFirstPublish())
            .setLocale(Locale.getDefault())
            .setTimeZone(TimeZone.getDefault())
            .build()
        val metaDataBuilder = MediaMetadata.Builder()
            .setTitle(musicTitle)
            .setArtist(bandName)
            .setArtworkUri(Uri.parse(getSongArtUrl()))
            .setComposer(composer)
            .setWriter(lyricist)
            .setConductor(arranger)
            .setReleaseDay(calendar.get(Calendar.DAY_OF_MONTH))
            .setReleaseMonth(calendar.get(Calendar.MONTH))
            .setReleaseYear(calendar.get(Calendar.YEAR))

        description?.let {
            metaDataBuilder.setDescription(ResourceMapper.localize(it))
        }
        return MediaItem.Builder()
            .setMediaMetadata(metaDataBuilder.build())
            .setMediaId(mediaId)
            .setUri(Uri.parse(getSongFileUrl()))
            .build()
    }


}