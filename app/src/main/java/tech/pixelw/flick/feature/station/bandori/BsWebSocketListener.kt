package tech.pixelw.flick.feature.station.bandori

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import tech.pixelw.flick.core.misc.LogUtil.e
import tech.pixelw.flick.core.misc.LogUtil.i

/**
 * @author Carl Su "Pixelw"
 * @date 2021/2/10
 */
class BsWebSocketListener(private val callback: Callback) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        i(TAG, "onOpen")
        callback.onWsOpen()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        callback.updateMessage(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        e(TAG, "unknown raw data, size=" + bytes.size)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        i(TAG, "closing: $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        i(TAG, "closed: $reason")
        callback.onWsClose()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        e(TAG, "fail: " + t.localizedMessage + (if (response != null) "response: " + response.message else ""))
        callback.onWsClose()
        callback.onFailure(t)
    }

    interface Callback {
        fun onWsClose()
        fun updateMessage(string: String?)
        fun onWsOpen()
        fun onFailure(throwable: Throwable?)
    }

    companion object {
        private const val TAG = "BsWebSocket"
    }
}
