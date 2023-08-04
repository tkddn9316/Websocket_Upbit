package com.example.websocket_upbit.data.retrofit

import com.example.websocket_upbit.data.model.Coin
import com.example.websocket_upbit.data.model.Market
import com.example.websocket_upbit.data.model.Ticker
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiV1 {
    @GET("v1/market/all")
    fun getMarket(): Single<List<Market>>

    @GET("v1/ticker")
    fun getTicker(
        @Query("markets") markets: String
    ): Single<List<Ticker>>

    @GET("v1/ticker")
    fun getTickers(
        @Query("markets") markets: List<String>
    ): Single<List<Ticker>>
}