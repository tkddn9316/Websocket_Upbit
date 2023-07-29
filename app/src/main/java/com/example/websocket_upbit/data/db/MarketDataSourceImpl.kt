package com.example.websocket_upbit.data.db

import com.example.websocket_upbit.data.model.Market
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MarketDataSourceImpl @Inject constructor(private val marketDao: MarketDao): MarketDataSource {
    override fun getAll(): Single<List<Market>> {
        return marketDao.getAll()
    }

    override fun insert(data: List<Market>): Completable {
        return marketDao.insert(data)
    }

    override fun deleteAll(): Completable {
        return marketDao.deleteAll()
    }
}