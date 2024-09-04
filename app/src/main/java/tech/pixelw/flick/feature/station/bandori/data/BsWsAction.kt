package tech.pixelw.flick.feature.station.bandori.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tech.pixelw.flick.core.json.SerializeNull

@Keep
@JsonClass(generateAdapter = true)
class BsWsAction(
    @Json val action: String,
    @Json @SerializeNull var data: MutableMap<String, Any>? = null
) {

    fun addData(key: String, value: Any) = apply {
        if (data == null) data = mutableMapOf()
        data!![key] = value
    }

    companion object {
        const val ACTION_INIT_CHAT: String = "initializeChatRoom"

        const val ACTION_LOAD_CHAT_LOG: String = "loadChatLog"

        const val ACTION_SET_PERMISSION: String = "setAccessPermission"

        //声明需要服务端传输房间列表
        const val ACTION_GET_ROOM_NUMBER: String = "getRoomNumberList"

        //服务端发送过来的Action以及客户端发送房间时候的Action
        const val ACTION_SEND_ROOM_LIST: String = "sendRoomNumberList"

        const val ACTION_SEND_CHAT: String = "sendChat"

        const val ACTION_SEND_ROOM_NUMBER: String = "sendRoomNumber"

        const val ACTION_HEARTBEAT: String = "heartbeat"

        const val ACTION_SET_CLIENT: String = "setClient"

        const val ACTION_GET_SERVER_TIME: String = "getServerTime"
    }

}