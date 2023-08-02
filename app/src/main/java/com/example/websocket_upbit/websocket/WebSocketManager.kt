package com.example.websocket_upbit.websocket

import com.example.websocket_upbit.data.model.Ticker
import com.example.websocket_upbit.data.retrofit.networkThread
import com.example.websocket_upbit.util.FConstrant
import com.example.websocket_upbit.util.FConstrant.getGson
import com.example.websocket_upbit.util.FLog
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.lang.reflect.Type
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

    private const val MAX_NUM = 3
    private const val MILLIS = 1000

    private val request: Request = Request.Builder().url(FConstrant.WSS_URL).build()
    private val compositeDisposable = CompositeDisposable()

    private var socket: WebSocket? = null
    private var dataListener: WebSocketDataListener? = null

    private var isConnect = false
    private var connectNum = 0
    private val gson = getGson()

    const val EVENT_PING = "PING"
    const val EVENT_PONG = "UP"
    const val EVENT_TICKER = "ticker"
    const val EVENT_TRADE = "trade"
    const val EVENT_ORDERBOOK = "orderbook"

    fun onOpen() {
        FLog.e("onOpen")
        socket?.let {
            dataListener?.onConnect()
        } ?: connect()
    }

    fun onClose() {
        FLog.e("onClose")
        socket?.close(1000, "close")
        compositeDisposable.clear()
        isConnect = false
        socket = null
    }

    fun setListener(listener: WebSocketDataListener) {
        onClose()
        this.dataListener = listener
        onOpen()
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
                FLog.e("onClosed")
                dataListener?.onClosed()
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                FLog.e("onClosing")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                t.printStackTrace()
                response?.message?.let { FLog.e(it) }
                dataListener?.onFailure()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                try {
                    val event = gson.fromJson(text, ReceiveEvent::class.java)
                    if (EVENT_PONG == event.status) {
                        FLog.e("onPong")
                        compositeDisposable.add(
                            Single.just(1)
                                .networkThread()
                                .delay(10, TimeUnit.SECONDS)
                                .subscribe({ onPing() }, {})
                        )
                    } else if (EVENT_TICKER == event.type) {
                        val data = gson.fromJson(text, Ticker::class.java)
                        dataListener?.onData(data)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                onMessage(webSocket, bytes.utf8())
            }
        })
    }

    fun reconnect() {
        if (connectNum <= MAX_NUM) {
            try {
                Thread.sleep(MILLIS.toLong())
                connect()
                connectNum++
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } else {
            onClose()
            FLog.e("reconnect over $MAX_NUM,please check url or network")
        }
    }

    fun onPing() {
//        send(SocketEvent(EVENT_PING, mutableListOf()))
        FLog.e("onPing")
        socket!!.send(EVENT_PING)
    }

    fun onMain() {
        FLog.e("onMain")
        // TODO: 일단 BTC만
        send(SocketEvent(EVENT_TICKER, mutableListOf("KRW-BTC")))
    }

    private fun send(event: SocketEvent): Boolean {
        val ticket = SocketEvent.Ticket("TEST_1234_")
        val format = SocketEvent.Format("DEFAULT")
        return socket?.let {
            val text = gson.toJson(arrayListOf(ticket, event, format))
            FLog.e(text)
            it.send(text)
        } ?: false
    }

    inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type
}