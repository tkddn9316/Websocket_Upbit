package com.example.websocket_upbit.websocket

import com.example.websocket_upbit.util.FConstrant
import com.example.websocket_upbit.util.FConstrant.getGson
import com.example.websocket_upbit.util.FLog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketManager {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .writeTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .pingInterval(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    private val request: Request = Request.Builder().url(FConstrant.WSS_URL).build()
    private val compositeDisposable = CompositeDisposable()

    private var socket: WebSocket? = null
    private var dataListener: WebSocketDataListener? = null

    private var isConnect = false
    private var connectNum = 0
    private val gson = getGson()

    const val EVENT_PING = "ping"
    const val EVENT_PONG = "pong"

    fun onOpen() {
        FLog.e("onOpen")
        socket?.let {
            dataListener?.onConnect()
        } ?: connect()
    }

    private fun connect() {
        FLog.e("connect")
        provideOkHttpClient().newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                FLog.d("onOpened : $response")
                socket = webSocket
                isConnect = response.code == 101
                if (!isConnect) {
                    reconnect()
                } else {
                    compositeDisposable.clear()
                    FLog.i("connect success.")
                    dataListener?.onConnect()
                    onPing()
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }
        })
    }

    fun onPing() {
        socket?.let {
            val text = gson.toJson()
        }
    }
}