package tech.pixelw.flick.feature.station.bandori

import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import tech.pixelw.flick.core.misc.LogUtil

/**
 * @author Carl Su "Pixelw"
 * @date 2021/2/10
 */
open class BsWebSocketWorker(private val okHttpClient: OkHttpClient) {
    private var running = false
    private var webSocket: WebSocket? = null
    var heartBeatCount: Int = 0
    var handler: Handler = Handler(Looper.getMainLooper())

    private var heartBeatThread: Runnable = object : Runnable {
        override fun run() {
            if (send(HEARTBEAT_STRING)) {
                LogUtil.i(TAG, "heartbeat ok: " + ++heartBeatCount)
            }
            handler.postDelayed(this, HEARTBEAT_INTERVAL.toLong())
        }
    }


    // repository回调
    var callback: Callback? = null

    // 下层 ws监听器
    private val webSocketListener: BsWebSocketListener by lazy {
        BsWebSocketListener(object : BsWebSocketListener.Callback {
            override fun onWsClose() {
                connecting = false
                toggleRunning(false)
            }

            override fun updateMessage(string: String?) {
                LogUtil.d(TAG, "recv:$string")
                callback?.onResponse(string)
            }

            override fun onWsOpen() {
                connecting = false
                toggleRunning(true)
            }

            override fun onFailure(throwable: Throwable?) {
                connecting = false
                callback?.onWsStatusChanged(throwable!!.localizedMessage)
            }
        })
    }

    //    List<String> sendWaitingQueue = new LinkedList<>();
    private var pendingSendText: String? = null
    private var connecting = false

    fun connectWs(): Boolean {
        if (connecting) {
            return true
        }
        val request = Request.Builder()
            .url(URL)
            .build()

        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
        connecting = true
        return true
    }


    fun disconnectWs() {
        webSocket?.close(1000, "disconnectWs invoked")
        toggleRunning(false)
    }

    fun send(text: String?): Boolean {
        // 当断开链接时尝试重连
        if (!running) {
            LogUtil.w(TAG, "send method trying to reconnect")
            //            sendWaitingQueue.add(text);
            pendingSendText = text
            return connectWs()
        }
        val success = webSocket!!.send(text!!)
        if (success) {
            LogUtil.i(TAG, "send: $text")
        } else {
            callback!!.onWsStatusChanged(WS_DISCONNECTED)
            LogUtil.e(TAG, "send failed...")
        }
        return success
    }

    fun checkPendingSend() {
        if (pendingSendText.isNullOrBlank().not()) {
            send(pendingSendText)
            pendingSendText = null
        }
    }


    private fun toggleRunning(nowRunning: Boolean) {
        val changed = running != nowRunning
        running = nowRunning
        //开关心跳
        if (nowRunning) {
            heartBeat()
        } else {
            stopHeartBeat()
        }
        if (changed) {
            callback!!.onWsStatusChanged(if (nowRunning) WS_CONNECTED else WS_DISCONNECTED)
        }
    }

    private fun heartBeat() {
        stopHeartBeat()
        handler.post(heartBeatThread)
    }

    private fun stopHeartBeat() {
        handler.removeCallbacks(heartBeatThread)
    }


    interface Callback {
        fun onResponse(response: String?)

        fun onWsStatusChanged(status: String?)
    }

    companion object {
        const val URL: String = "wss://api.bandoristation.com"
        const val HEARTBEAT_INTERVAL: Int = 25 * 1000
        const val HEARTBEAT_STRING: String = "{action: \"heartbeat\", data: {client: \"Bandroid\"}}"
        private const val TAG = "BsWebSocketWorker"
        const val WS_CONNECTED: String = "connected"
        const val WS_DISCONNECTED: String = "disconnected"
    }
}
