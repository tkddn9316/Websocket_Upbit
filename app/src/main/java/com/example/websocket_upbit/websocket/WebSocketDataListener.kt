package com.example.websocket_upbit.websocket

interface WebSocketDataListener {
    fun onConnect()
    fun onClosed()
    fun onFailure()
}