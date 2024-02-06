package tech.pixelw.flick.common.resources

import android.content.Context
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.R

object ResourceMapper {
    fun getBandNameString(bandId: Int): String {
        val context: Context = FlickApp.context
        return when (bandId) {
            BangDream.Band.PPP.key -> context.getString(R.string.ppp)
            BangDream.Band.AG.key -> context.getString(R.string.ag)
            BangDream.Band.HHW.key -> context.getString(R.string.hhw)
            BangDream.Band.PP.key -> context.getString(R.string.pp)
            BangDream.Band.R.key -> context.getString(R.string.r)
            BangDream.Band.MOR.key -> context.getString(R.string.mnk)
            BangDream.Band.RAS.key -> context.getString(R.string.ras)
            BangDream.Band.MYGO.key -> context.getString(R.string.mygo)
            BangDream.Band.BAND_MIX.key -> context.getString(R.string.mixed_event)
            else -> "???"
        }
    }
}