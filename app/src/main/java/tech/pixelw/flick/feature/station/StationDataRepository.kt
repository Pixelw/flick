package tech.pixelw.flick.feature.station

import tech.pixelw.flick.common.apis
import tech.pixelw.flick.feature.station.bandori.data.BsRoom

class StationDataRepository {

    private val api = apis(BsWebApi::class.java, "bandoriStationApi")
    suspend fun getRoomNumber(): List<BsRoom> {
        val api = api.await() ?: return emptyList()
        return api.getRooms()
    }


}
