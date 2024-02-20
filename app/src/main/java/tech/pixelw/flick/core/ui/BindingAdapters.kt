package tech.pixelw.flick.core.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("coilSrc")
    fun coilSrc(imageView: ImageView, url: String?) {
        imageView.load(url)
    }
}
