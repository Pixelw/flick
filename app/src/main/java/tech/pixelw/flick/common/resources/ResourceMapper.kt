package tech.pixelw.flick.common.resources

import android.content.Context
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.R


object ResourceMapper {
    @JvmStatic
    fun localize(collection: Collection<String>?): String {
        if (collection.isNullOrEmpty()) return ""
        val jpnRes = collection.elementAtOrElse(0) { "" }
        if (collection.size != 5) {
            return jpnRes
        }

        return collection.elementAtOrElse(getLocaleIndex()) { jpnRes }
    }

    @JvmStatic
    fun getBandNameString(bandId: Int): String {
        val context: Context = FlickApp.context
        return when (bandId) {
            BanGDream.Band.PPP.key -> context.getString(R.string.ppp)
            BanGDream.Band.AG.key -> context.getString(R.string.ag)
            BanGDream.Band.HHW.key -> context.getString(R.string.hhw)
            BanGDream.Band.PP.key -> context.getString(R.string.pp)
            BanGDream.Band.R.key -> context.getString(R.string.r)
            BanGDream.Band.MOR.key -> context.getString(R.string.mnk)
            BanGDream.Band.RAS.key -> context.getString(R.string.ras)
            BanGDream.Band.MYGO.key -> context.getString(R.string.mygo)
            BanGDream.Band.BAND_MIX.key -> context.getString(R.string.mixed_event)
            else -> "???"
        }
    }

    private fun getLocaleIndex(): Int {
        val language = FlickApp.context.resources.configuration.locales[0]!!.toString().lowercase()
        return when {
            language.contains("cn") -> {
                BanGDream.Server.CHN.key
            }

            language.contains("tw") -> {
                BanGDream.Server.TPE.key
            }

            language.contains("kr") -> {
                BanGDream.Server.KOR.key
            }

            language.contains("en") -> {
                BanGDream.Server.GLO.key
            }

            else -> BanGDream.Server.JPN.key
        }
    }

    private const val TAG = "ResourceMapper"
}