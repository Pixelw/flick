package tech.pixelw.flick.common

import tech.pixelw.flick.common.resources.ResourceConfig
import tech.pixelw.flick.core.network.NetApiFactory

/**
 * Lazy方式创建, 并且根据[ResourceConfig.netApiRoot]自动切换
 */
fun <T> apis(cls: Class<T>, mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED): Lazy<T> {
    return lazy(mode) {
        NetApiFactory.get(cls, ResourceConfig.netApiRoot)
    }
}