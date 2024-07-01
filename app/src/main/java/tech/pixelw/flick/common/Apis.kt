@file:Suppress("UNCHECKED_CAST")

package tech.pixelw.flick.common

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import tech.pixelw.flick.common.resources.PresetHosts
import tech.pixelw.flick.common.resources.ResourceHostRepository
import tech.pixelw.flick.core.network.NetApiFactory


/**
 * for lapi only
 *
 * ```
 * val api by lapi(MusicNetApi::class.java)
 * ```
 */
fun <T> lapi(
    cls: Class<T>,
    baseUrl: String = PresetHosts.LAPI.baseUrl,
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED
): Lazy<T> {
    return lazy(mode) {
        NetApiFactory.get(cls, baseUrl)
    }
}


/**
 * 获取动态下发的host来生成api, deferred
 */
fun <T> apis(
    cls: Class<T>,
    apiType: String = ResourceHostRepository.BASE_API,
): Deferred<T?> {
    val deferredApi = MainScope().async {
        getApis(cls, apiType)
    }
    return deferredApi
}

/**
 * 获取动态下发的host来生成api
 */
suspend fun <T> getApis(
    cls: Class<T>,
    apiType: String = ResourceHostRepository.BASE_API,
): T? {
    val host = ResourceHostRepository.getConfigForKey(apiType)?.getDefaultHost() ?: return null
    return NetApiFactory.get(cls, host.baseUrl)
}