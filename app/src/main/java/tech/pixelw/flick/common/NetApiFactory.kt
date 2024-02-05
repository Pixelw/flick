@file:Suppress("UNCHECKED_CAST")

package tech.pixelw.flick.common

import retrofit2.Retrofit
import tech.pixelw.flick.common.network.MoshiConverter

object NetApiFactory {

    const val HOST_PIXEL_LAPI = 1

    private val retrofitCacheMap = mutableMapOf<Class<*>, Any>()

    fun <T> get(api: Class<T>, hostType: Int = HOST_PIXEL_LAPI): T {
        return if (retrofitCacheMap.containsKey(api)) {
            retrofitCacheMap[api] as T
        } else {
            Retrofit.Builder().callFactory(SharedOkhttpClient.DEFAULT)
                .addConverterFactory(MoshiConverter.DEFAULT)
                .baseUrl(getHostUrl(hostType))
                .build().create(api)
        }
    }

    private fun getHostUrl(hostType: Int): String {
        return "https://lapi.pixelw.tech/bandroid"
    }

}