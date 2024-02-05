package tech.pixelw.flick.common

import okhttp3.OkHttpClient

object SharedOkhttpClient {

    val DEFAULT = OkHttpClient.Builder().build()

}