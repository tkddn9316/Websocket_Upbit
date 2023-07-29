package com.example.websocket_upbit.data.db

import com.example.websocket_upbit.data.model.Market
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface MarketDataSource {
    fun getAll(): Single<List<Market>>
    fun insert(data: List<Market>): Completable
    fun deleteAll(): Completable
}