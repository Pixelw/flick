package tech.pixelw.flick.common.resources

import android.content.Context
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.R

object ResourceMapper {
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
}