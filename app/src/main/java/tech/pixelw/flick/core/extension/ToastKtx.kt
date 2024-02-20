package tech.pixelw.flick.core.extension

import android.widget.Toast
import tech.pixelw.flick.FlickApp

fun toast(string: String) {
    Toast.makeText(FlickApp.context, string, Toast.LENGTH_LONG).show()
}