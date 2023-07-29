package com.example.websocket_upbit.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

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
}