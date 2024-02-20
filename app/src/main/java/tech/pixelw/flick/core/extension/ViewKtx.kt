package tech.pixelw.flick.core.extension

import android.content.res.Resources


val Number.toPx get() = (this.toDouble() * Resources.getSystem().displayMetrics.density).toInt()
val Number.toDip get() = (this.toDouble() / Resources.getSystem().displayMetrics.density).toInt()