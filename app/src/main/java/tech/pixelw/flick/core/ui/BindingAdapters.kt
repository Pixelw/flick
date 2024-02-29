package tech.pixelw.flick.core.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.slider.Slider


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("coilSrc")
    fun coilSrc(imageView: ImageView, url: String?) {
        imageView.load(url)
    }

    @JvmStatic
    @BindingAdapter("showWhen", "isGone", requireAll = true)
    fun setVisibility(view: View, isShow: Boolean, isGone: Boolean) {
        view.visibility = when {
            isShow -> View.VISIBLE
            isGone -> View.GONE
            else -> View.INVISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("pgShowWhen")
    fun setVisibility(progressIndicator: BaseProgressIndicator<*>, isShow: Boolean) {
        if (isShow) {
            progressIndicator.show()
        } else {
            progressIndicator.hide()
        }
    }

    @JvmStatic
    @BindingAdapter("sliderCustomListener")
    fun setOnValueChangedListener(slider: Slider, onSlideChangedListener: OnSliderChangedListener) {
        val customListener = object : Slider.OnChangeListener, Slider.OnSliderTouchListener {
            private var value = 0f
            override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                // 只记录用户操作
                if (fromUser) this.value = value
            }

            override fun onStartTrackingTouch(slider: Slider) {
                // 需要在滑动时取消slider刷新, 所以设置一个负值
                onSlideChangedListener.onChanged(-1.0f)
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // 松手时再回调
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