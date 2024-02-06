package tech.pixelw.flick.common.network

import okhttp3.OkHttpClient

object SharedOkhttpClient {

    val DEFAULT = OkHttpClient.Builder().build()

}