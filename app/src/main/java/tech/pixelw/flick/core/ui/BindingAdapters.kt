package tech.pixelw.flick.core.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.slider.Slider


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("coilSrc")
    fun coilSrc(imageView: ImageView, url: String?) {
        imageView.load(url)
    }

    @JvmStatic
    @BindingAdapter("sliderCustomListener")
    fun setOnValueChangedListener(slider: Slider, onSlideChangedListener: OnSliderChangedListener) {
        val customListener = object : Slider.OnChangeListener, Slider.OnSliderTouchListener {
            var value = 0f
            override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                if (fromUser) this.value = value
            }

            override fun onStartTrackingTouch(slider: Slider) {
                onSlideChangedListener.onChanged(-1.11f)
            }

            override fun onStopTrackingTouch(slider: Slider) {
                onSlideChangedListener.onChanged(value)
            }
        }
        slider.addOnChangeListener(customListener)
        slider.addOnSliderTouchListener(customListener)
    }


    interface OnSliderChangedListener {
        fun onChanged(v: Float)
    }

}