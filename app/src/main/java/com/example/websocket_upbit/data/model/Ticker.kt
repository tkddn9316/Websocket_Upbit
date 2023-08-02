package com.example.websocket_upbit.data.model

// 웹소켓 현재가
// https://docs.upbit.com/reference/websocket-ticker
data class Ticker(
    var type: String,
    var code: String,
    var opening_price: Double,
    var high_price: Double,
    var low_price: Double,
    var trade_price: Double,
    var prev_closing_price: Double,
    var change: String,
    var timestamp: Long,
)
