package com.example.websocket_upbit.domain.repository

import com.example.websocket_upbit.data.db.MarketDataSource
import com.example.websocket_upbit.data.model.Market
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(private val data: MarketDataSource) :
    MarketRepository {
    override fun getAll(): Single<List<Market>> {
        return data.getAll()
    }

    override fun insert(data: List<Market>): Completable {
        return this.data.insert(data)
    }

    override fun deleteAll(): Completable {
        return data.deleteAll()
    }
}