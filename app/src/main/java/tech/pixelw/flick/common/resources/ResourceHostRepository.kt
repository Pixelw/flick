package tech.pixelw.flick.common.resources

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import tech.pixelw.flick.common.lapi

object ResourceHostRepository {

    const val BASE_API = "bandroid"

    var configFetchJob: Job? = null
    private var hostConfigMap: Map<String, HostConfig> = emptyMap()
    private val api by lapi(ResourceHostApi::class.java)

    internal fun fetchHostConfig() {
        configFetchJob = MainScope().launch {
            hostConfigMap = api.hostConfig()
            hostConfigMap[BASE_API]?.let { hostConfig ->
                val host = hostConfig.hostList.find { it.name == hostConfig.default } ?: return@let
                ResourceConfig.setBaseApi(host)
            }
        }
    }

    suspend fun getConfigForKey(key: String): HostConfig? {
        configFetchJob?.join()
        return hostConfigMap[key]
    }
}