package tech.pixelw.flick.common.resources

import retrofit2.http.GET

interface ResourceHostApi {

    @GET("host_config.json")
    suspend fun hostConfig(): ResourceHostConfig

}
