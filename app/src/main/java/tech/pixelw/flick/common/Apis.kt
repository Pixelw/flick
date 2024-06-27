package tech.pixelw.flick.common

import tech.pixelw.flick.common.resources.PresetHosts
import tech.pixelw.flick.common.resources.ResourceConfig
import tech.pixelw.flick.core.network.NetApiFactory

/**
 * Lazy方式创建, 默认根据[ResourceConfig.netApiDefault]自动切换
 */
fun <T> apis(
    cls: Class<T>,
    host: PresetHosts = ResourceConfig.netApiDefault,
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED
): Lazy<T> {
    return lazy(mode) {
        NetApiFactory.get(cls, host.baseUrl)
    }
}