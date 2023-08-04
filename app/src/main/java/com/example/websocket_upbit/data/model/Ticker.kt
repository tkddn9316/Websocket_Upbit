package com.example.websocket_upbit.data.model

import android.content.Context
import com.example.websocket_upbit.R
import com.example.websocket_upbit.util.FConstrant
import com.example.websocket_upbit.util.FConstrant.toCommaPointFormat

// 웹소켓 현재가
// https://docs.upbit.com/reference/websocket-ticker
data class Ticker(
    var type: String,
    var market: String,
    var code: String,   // market = code
    var opening_price: Double,
    var high_price: Double,
    var low_price: Double,
    var trade_price: Double,
    var prev_closing_price: Double,
    var change: String,
    var korean_name: String = "",
    var ask_bid: String,    // 매수/매도
    var timestamp: Long,
) {

    var rate: Double = 0.0
        get() =
            if (prev_closing_price <= 0) 0.0
            else {
                trade_price.minus(prev_closing_price).div(prev_closing_price).times(100)
            }

    var isPlus: Int = 0
        get() = trade_price.compareTo(prev_closing_price)

    var lazy: Boolean = false

    fun setPrice(data: Double): String {
        return if (data >= 100) data.toCommaPointFormat() else data.toCommaPointFormat(2)
    }

    fun setColor(context: Context): Int {
        return when(change) {
            FConstrant.PriceColor.RISE.name -> context.getColor(R.color.red_500)
            FConstrant.PriceColor.FALL.name -> context.getColor(R.color.blue_500)
            else -> context.getColor(R.color.black_700)
        }
    }

    fun setPercent(): String {
        val value = rate.toCommaPointFormat(2)

        return when {
            isPlus > 0 -> {
                "+".plus(value)
            }
            isPlus < 0 -> {
                value
            }
            else -> {
                "0"
            }
        }.plus("%")
    }

    fun addValue(market: String, korean_name: String) {
        this.market = market
        this.korean_name = korean_name
    }
}
