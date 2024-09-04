package tech.pixelw.flick.feature.station

import com.squareup.moshi.adapter
import org.json.JSONObject
import tech.pixelw.flick.common.apis
import tech.pixelw.flick.core.json.moshi
import tech.pixelw.flick.core.json.moshiSerializeNull
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.core.network.SharedOkhttpClient
import tech.pixelw.flick.feature.station.bandori.BsWebSocketWorker
import tech.pixelw.flick.feature.station.bandori.data.BsBaseEntity
import tech.pixelw.flick.feature.station.bandori.data.BsMessage
import tech.pixelw.flick.feature.station.bandori.data.BsRoom
import tech.pixelw.flick.feature.station.bandori.data.BsWsAction
import tech.pixelw.flick.feature.station.bandori.data.BsWsMessageList


class StationDataRepository {

    private val api = apis(BsWebApi::class.java, "bandoriStationApi")

    private var wsWorker: BsWebSocketWorker? = null

    private var configStatus = ConfigStatus()

    suspend fun getRoomNumber(): List<BsRoom> {
        val api = api.await() ?: return emptyList()
        return api.getRooms().response ?: emptyList()
    }

    fun connectToWs(callback: ((comingList: List<BsBaseEntity>, action: String) -> Unit)) {
        wsWorker = BsWebSocketWorker(SharedOkhttpClient.fallbackOkHttpClient)
        wsWorker!!.callback = object : BsWebSocketWorker.Callback {
            override fun onResponse(response: String) {
                try {
                    val jsonObj = JSONObject(response)
                    val status = jsonObj.getString("status")
                    if ("success" == status) {
                        parseWsAction(jsonObj, callback)
                    } else {
                        LogUtil.e("json status fail: $response")
                    }
                } catch (t: Throwable) {
                    LogUtil.e(t)
                }
            }

            override fun onWsStatusChanged(status: String) {
                if (BsWebSocketWorker.WS_CONNECTED == status) {
                    sendInitSettings()
                    wsWorker?.checkPendingSend()
                }
            }

        }
        wsWorker!!.connectWs()
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun sendInitSettings() {
        val initialSettings = mutableListOf(
            BsWsAction(BsWsAction.ACTION_SET_CLIENT).addData("client", "Flick").addData("send_room_number", true),
        )
        if (!configStatus.initRoomNumberSent) {
            initialSettings.add(BsWsAction(BsWsAction.ACTION_GET_ROOM_NUMBER))
        }
        // TODO: SLY 24/9/4 login

        wsWorker?.let {
            it.send(moshiSerializeNull.adapter<List<BsWsAction>>().nullSafe().toJson(initialSettings))
            initialSettings.forEach { ds ->
                when (ds.action) {
                    BsWsAction.ACTION_INIT_CHAT -> {
                        configStatus.initialChatHasSent = true
                    }

                    BsWsAction.ACTION_GET_ROOM_NUMBER -> {
                        configStatus.initRoomNumberSent = true
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun parseWsAction(jsonObj: JSONObject, callback: (List<BsBaseEntity>, String) -> Unit) {
        val action = jsonObj.getString("action")
        when (action) {
            BsWsAction.ACTION_SEND_ROOM_LIST -> {
                val adapter = moshi.adapter<List<BsRoom>>()
                val list = adapter.fromJson(getArr(jsonObj)) ?: return
                for (bsRoom in list) {
                    if (bsRoom.message.contains(bsRoom.number)) {
                        bsRoom.message = bsRoom.message.replace(bsRoom.number, "").trim()
                    }
                }
                callback(list, action)
            }

            BsWsAction.ACTION_INIT_CHAT, BsWsAction.ACTION_LOAD_CHAT_LOG -> {
                val adapter = moshi.adapter<BsWsMessageList>()
                val list = adapter.fromJson(getObj(jsonObj)) ?: return
                callback(list.messageList, action)
            }

            BsWsAction.ACTION_SEND_CHAT -> {
                val adapter = moshi.adapter<List<BsMessage>>()
                val list = adapter.fromJson(getObj(jsonObj)) ?: return
                callback(list, action)
            }
        }
    }

    private fun getObj(jsonObj: JSONObject) = jsonObj.getJSONObject("response").toString()

    private fun getArr(jsonObj: JSONObject) = jsonObj.getJSONArray("response").toString()


    class ConfigStatus {
        var lastDisconnectTime: Long = Long.MAX_VALUE

        // 当此值为true， 则表示主list中的值无效，等待清除
        var purgeMasterList: Boolean = true
        var earliestMsgTime: Long = Long.MAX_VALUE
        var latestMsgTime: Long = Long.MIN_VALUE

        var initialChatHasSent: Boolean = false
        var initRoomNumberSent: Boolean = false

        init {
            LogUtil.w("reset station")
        }
    }


}
