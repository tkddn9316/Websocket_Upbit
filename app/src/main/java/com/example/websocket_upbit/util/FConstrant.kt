package com.example.websocket_upbit.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.math.BigDecimal
import java.text.DecimalFormat

object FConstrant {
    const val BASE_URL = "https://api.upbit.com/"
    const val WSS_URL = "wss://api.upbit.com/websocket/v1"

    @JvmStatic
    fun getGson(): Gson {
        val builder = GsonBuilder()
//        builder.registerTypeAdapter(DateTime::class.java, DateTimeTypeConverter())
        //        builder.registerTypeAdapter(Boolean.class, new BooleanTypeConverter());
        return builder.create()
    }

    fun Int.toCommaPointFormat(scale: Int = -1): String {
        return DecimalFormat(getPointPattern(scale)).format(this)
    }

    fun Double.toCommaPointFormat(scale: Int = -1): String {
        return DecimalFormat(getPointPattern(scale)).format(this)
    }

    private fun getPointPattern(scale: Int): String {
        return when (scale) {
            0 -> "#,##0"
            1 -> "#,##0.0"
            2 -> "#,##0.00"
            3 -> "#,##0.000"
            4 -> "#,##0.0000"
            5 -> "#,##0.00000"
            6 -> "#,##0.000000"
            7 -> "#,##0.0000000"
            8 -> "#,##0.00000000"
            else -> "#,###.########"
        }
    }

    enum class PriceColor {
        RISE, EVEN, FALL
    }

    enum class SellColor(val value: String) {
        BUY("BID"), SELL("ASK")
    }
}