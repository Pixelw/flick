package tech.pixelw.flick

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class FlickApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var appName: String
    }

    override fun onCreate() {
        context = applicationContext
        super.onCreate()
        appName = context.getString(R.string.app_name)
    }
}