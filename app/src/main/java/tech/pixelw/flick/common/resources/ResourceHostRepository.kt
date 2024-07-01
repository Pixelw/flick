package tech.pixelw.flick.common.resources

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import tech.pixelw.flick.common.getApis
import tech.pixelw.flick.core.network.moshi

object ResourceHostRepository {

    const val BASE_API = "bandroid"

    var configFetchJob: Job? = null
    private var hostConfigMap: Map<String, HostConfig> = emptyMap()

    internal fun fetchHostConfig() {
        configFetchJob = MainScope().launch {
            val api = getApis(ResourceHostApi::class.java) ?: return@launch
            hostConfigMap = api.hostConfig()
            hostConfigMap[BASE_API]?.let { config ->
                val adapter = moshi.adapter(HostConfig::class.java)
                val hostConfig = adapter.fromJsonValue(config) ?: return@let
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