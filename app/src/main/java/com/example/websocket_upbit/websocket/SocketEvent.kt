package com.example.websocket_upbit.websocket

data class SocketEvent(
    var type: String,
    var codes: List<String>,
) {
    data class Ticket(
        var ticket: String
    )

    data class Format(
        var format: String
    )
}