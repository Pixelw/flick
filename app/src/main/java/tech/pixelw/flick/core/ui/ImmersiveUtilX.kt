package tech.pixelw.flick.core.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import tech.pixelw.flick.core.extension.toDip
import tech.pixelw.flick.core.misc.LogUtil
import kotlin.math.max

@RequiresApi(21)
object ImmersiveUtilX {

    private const val TAG = "ImmersiveUtilX"
    const val VIEW_MARGIN = 1
    const val VIEW_PADDING = 2
    const val VIEW_DIMEN = 3

    @IntDef(value = [VIEW_DIMEN, VIEW_PADDING, VIEW_MARGIN])
    internal annotation class ViewType

    @JvmStatic
    fun applyFullscreen(window: Window, viewForIme: View) {
        val windowInsetsControllerCompat = WindowInsetsControllerCompat(window, viewForIme)
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsControllerCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    @JvmStatic
    fun isDarkModeEnabled(context: Context): Boolean {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    @JvmStatic
    fun setLightStatusBar(window: Window, light: Boolean) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = light
    }

    class Edge2EdgeBuilder(private val context: Context, private val window: Window) {
        private var viewAtTop: View? = null
        private var topViewType: Int? = null
        private var viewAtBottom: View? = null
        private var bottomViewType: Int? = null
        private var forceNavTransparent = false
        private var listener: OnInsetsChangeListener? = null

        fun viewAtTop(viewAtTop: View, @ViewType type: Int) = apply {
            this.viewAtTop = viewAtTop
            topViewType = type
        }

        fun viewAtBottom(viewAtBottom: View, @ViewType type: Int) = apply {
            this.viewAtBottom = viewAtBottom
            bottomViewType = type
        }

        fun forceNavBarTransparent(trans: Boolean) = apply {
            forceNavTransparent = trans
        }

        fun addListener(listener: OnInsetsChangeListener) = apply {
            this.listener = listener
        }


        fun apply() {
            try {
                applyInternal()
            } catch (e: Exception) {
                Log.e(TAG, "error on applying Edge2Edge", e)
            }
        }

        private fun applyInternal() {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Color.TRANSPARENT
            var contentView = window.decorView.findViewById<View>(android.R.id.content)
            if (contentView == null) {
                LogUtil.e(TAG, "could not found content view")
                contentView = window.decorView
            }
            ViewCompat.setOnApplyWindowInsetsListener(contentView) { _, insets ->
                val insetSys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                val insetNav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                val insetIme = insets.getInsets(WindowInsetsCompat.Type.ime())
                val insetCutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                Log.i(
                    TAG,
                    "Ime status: $insetIme \n system: $insetSys \n displaycutout: $insetCutout"
                )
                applyNavbar(insetNav)
                viewAtTop?.apply {
                    val top = max(insetSys.top, insetIme.top)
                    when (topViewType) {
                        VIEW_PADDING -> {
                            setPadding(paddingLeft, top, paddingRight, paddingBottom)
                        }

                        VIEW_MARGIN -> {
                            val params = layoutParams as? ViewGroup.MarginLayoutParams
                            params?.setMargins(
                                params.leftMargin,
                                top,
                                params.rightMargin,
                                params.bottomMargin
                            )
                            layoutParams = params
                        }

                        VIEW_DIMEN -> {
                            layoutParams.height = top
                        }
                    }
                }
                viewAtBottom?.apply {
                    val bottom = max(insetSys.bottom, insetIme.bottom)
                    when (bottomViewType) {
                        VIEW_PADDING -> {
                            setPadding(paddingLeft, paddingTop, paddingRight, bottom)
                        }

                        VIEW_MARGIN -> {
                            val params = layoutParams as? ViewGroup.MarginLayoutParams
                            params?.setMargins(
                                params.leftMargin,
                                params.topMargin,
                                params.rightMargin,
                                bottom
                            )
                            layoutParams = params
                        }

                        VIEW_DIMEN -> {
                            layoutParams.height = bottom
                        }
                    }
                }
                dispatchInsets(insetSys, insetIme, insetCutout)
                insets
            }

        }


        private fun applyNavbar(insetNav: Insets) {
            window.navigationBarColor = if (forceNavTransparent) {
                Color.TRANSPARENT
            } else {
                val size = listOf(insetNav.top, insetNav.bottom, insetNav.left, insetNav.right)
                    .maxOrNull() ?: 0
                if (size.toDip > 30) {
                    if (isDarkModeEnabled(context)) {
                        WindowInsetsControllerCompat(
                            window,
                            window.decorView
                        ).isAppearanceLightNavigationBars = false
                        0x80000000.toInt()
                    } else {
                        WindowInsetsControllerCompat(
                            window,
                            window.decorView
                        ).isAppearanceLightNavigationBars = true
                        0x80ffffff.toInt()
                    }
                } else {
                    Color.TRANSPARENT
                }

            }
        }

        private var lastSysInset: Insets? = null
        private var lastImeInset: Insets? = null
        private var lastCutoutInset: Insets? = null

        private fun dispatchInsets(
            insetSys: Insets,
            insetIme: Insets,
            insetCutout: Insets
        ) {
            listener?.run {
                if (insetSys != lastSysInset) {
                    onSystemBarsInsets(insetSys)
                    lastSysInset = insetSys
                }
                if (insetIme != lastImeInset) {
                    onImeInsets(insetIme)
                    lastImeInset = insetIme
                }
                if (insetCutout != lastCutoutInset) {
                    val safeZoneInset = Insets.max(insetSys, insetCutout)
                    onSafeZoneInsets(safeZoneInset)
                    lastCutoutInset = safeZoneInset
                }
            }
        }


    }

}