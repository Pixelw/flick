@file:Suppress("UNCHECKED_CAST")

package tech.pixelw.flick.core.network

import org.jetbrains.annotations.ApiStatus.Internal
import retrofit2.Retrofit

object NetApiFactory {

    private val retrofitCacheMap = mutableMapOf<String, Any>()

    @Internal
    fun <T> get(api: Class<T>, baseUrl: String): T {
        val key = api.name + baseUrl
        return if (retrofitCacheMap.containsKey(key)) {
            retrofitCacheMap[key] as T
        } else {
            val create = Retrofit.Builder()
                .callFactory(SharedOkhttpClient.DEFAULT)
                .addConverterFactory(MoshiConverterFactory.DEFAULT)
                .baseUrl(baseUrl)
                .build()
                .create(api)
            retrofitCacheMap[key] = create!!
            create
        }
    }

}