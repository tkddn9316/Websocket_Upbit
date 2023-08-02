package com.example.websocket_upbit.websocket

import com.example.websocket_upbit.data.model.Ticker

interface WebSocketDataListener {
    fun onConnect()
    fun onClosed()
    fun onFailure()

    fun onData(data: Ticker)
}