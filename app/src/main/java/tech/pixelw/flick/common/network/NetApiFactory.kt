@file:Suppress("UNCHECKED_CAST")

package tech.pixelw.flick.common.network

import retrofit2.Retrofit

object NetApiFactory {

    const val HOST_PIXEL_LAPI = 1

    private val retrofitCacheMap = mutableMapOf<String, Any>()

    fun <T> get(api: Class<T>, hostType: Int = HOST_PIXEL_LAPI): T {
        val key = api.name + hostType
        return if (retrofitCacheMap.containsKey(key)) {
            retrofitCacheMap[key] as T
        } else {
            val create = Retrofit.Builder().callFactory(SharedOkhttpClient.DEFAULT)
                .addConverterFactory(MoshiConverter.DEFAULT)
                .baseUrl(getHostUrl(hostType))
                .build().create(api)
            retrofitCacheMap[key] = create!!
            create
        }
    }

    private fun getHostUrl(hostType: Int): String {
        return "https://lapi.pixelw.tech/bandroid/"
    }

}