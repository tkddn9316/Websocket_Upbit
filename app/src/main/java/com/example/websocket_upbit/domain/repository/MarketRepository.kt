package com.example.websocket_upbit.domain.repository

import com.example.websocket_upbit.data.model.Market
import com.example.websocket_upbit.data.model.Ticker
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface MarketRepository {
    fun getAll(): Single<List<Market>>
    fun insert(data: List<Market>): Completable
    fun deleteAll(): Completable
//    fun getItems(): Single<List<Ticker>>
    fun getTickEventPublisher(): Flowable<List<Ticker>>
    fun updateTick(data: Ticker)
}