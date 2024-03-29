package tech.pixelw.flick.core.misc

import android.util.Log
import tech.pixelw.flick.BuildConfig
import tech.pixelw.flick.FlickApp

/**
 * @author Carl Su
 * @date 2019/12/19
 */
object LogUtil {
    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private const val NOTHING = 6
    private const val NULL_LOG_MSG = "!(null log msg)"
    private const val EMPTY_LOG_MSG = "!(empty log msg)"

    //set project logging level here
    var level = if (BuildConfig.DEBUG) VERBOSE else INFO

    fun v(msg: String?, tag: String = FlickApp.appName) {
        if (level <= VERBOSE) {
            Log.v(tag, avoidNullMsg(msg))
        }
    }

    fun d(msg: String?, tag: String = FlickApp.appName) {
        if (level <= DEBUG) {
            Log.d(tag, avoidNullMsg(msg))
        }
    }

    fun i(msg: String?, tag: String = FlickApp.appName) {
        if (level <= INFO) {
            Log.i(tag, avoidNullMsg(msg))
        }
    }

    fun w(msg: String?, tag: String = FlickApp.appName) {
        if (level <= WARN) {
            Log.w(tag, avoidNullMsg(msg))
        }
    }

    fun e(msg: String?, tag: String = FlickApp.appName) {
        if (level <= ERROR) {
            Log.e(tag, avoidNullMsg(msg))
        }
    }

    fun w(msg: String?, throwable: Throwable? = null, tag: String = FlickApp.appName) {
        if (level <= WARN) {
            Log.w(tag, avoidNullMsg(msg), throwable)
        }
    }

    fun e(msg: String?, throwable: Throwable? = null, tag: String = FlickApp.appName) {
        if (level <= ERROR) {
            Log.e(tag, avoidNullMsg(msg), throwable)
        }
    }

    fun e(throwable: Throwable, tag: String = FlickApp.appName) {
        if (level <= ERROR) {
            Log.e(tag, throwable.localizedMessage, throwable)
        }
    }

    private fun avoidNullMsg(msg: String?): String {
        if (msg == null) {
            return NULL_LOG_MSG
        }
        return if (msg.isEmpty()) {
            EMPTY_LOG_MSG
        } else msg
    }
}