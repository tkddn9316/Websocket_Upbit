package com.example.websocket_upbit.data.model

import java.math.BigDecimal

data class Coin(
    var market: String,
    var opening_price: BigDecimal = BigDecimal.ZERO,
    var high_price: BigDecimal = BigDecimal.ZERO,
    var low_price: BigDecimal = BigDecimal.ZERO,
    var trade_price: BigDecimal = BigDecimal.ZERO,
    var prev_closing_price: BigDecimal = BigDecimal.ZERO,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
//    var market: String,
)