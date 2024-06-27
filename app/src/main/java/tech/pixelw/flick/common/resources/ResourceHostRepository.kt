package tech.pixelw.flick.common.resources

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import tech.pixelw.flick.common.apis

class ResourceHostRepository {
    private val api by apis(ResourceHostApi::class.java)

    fun fetchHostConfig() {
        MainScope().launch {
            val hostConfig = api.hostConfig()
            ResourceConfig.applyConfig(hostConfig)
        }
    }
}