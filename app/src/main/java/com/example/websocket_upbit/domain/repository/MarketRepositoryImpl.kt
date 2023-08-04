package com.example.websocket_upbit.domain.repository

import com.example.websocket_upbit.data.db.MarketDataSource
import com.example.websocket_upbit.data.model.Market
import com.example.websocket_upbit.data.model.Ticker
import com.example.websocket_upbit.util.FLog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(private val data: MarketDataSource) : MarketRepository {

    private val coinList = ConcurrentHashMap<String, Ticker>()
    private val tickEventPublisher = PublishSubject.create<List<Ticker>>().toSerialized()

    override fun getAll(): Single<List<Market>> {
        return data.getAll()
    }

    override fun insert(data: List<Market>): Completable {
        return this.data.insert(data)
    }

    override fun deleteAll(): Completable {
        return data.deleteAll()
    }

//    override fun getItems(): Single<List<Ticker>> {
//        return if (coinList.size > 0) {
//            FLog.e("있")
//            val data = coinList.values.filterNot { it.code == "a" }
//            Single.just(data)
//        } else {
//            FLog.e("없")
//            Single.just(emptyList())
//        }
//    }

    override fun getTickEventPublisher(): Flowable<List<Ticker>> {
//        FLog.d("getTickEventPublisher : " + data.code)
        return tickEventPublisher.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { it.printStackTrace() }
    }

    override fun updateTick(data: Ticker) {
        coinList[data.code] = data
        coinList[data.code]?.also {
            it.change = data.change
            it.high_price = data.high_price
            it.low_price = data.low_price
            it.opening_price = data.opening_price
            it.prev_closing_price = data.prev_closing_price
            it.trade_price = data.trade_price
            it.timestamp = data.timestamp
            it.type = data.type
            it.market = data.code
            it.korean_name = data.korean_name
            it.ask_bid = data.ask_bid
            it.lazy = true

//            coinList[data.code] = data
        }
        tickEventPublisher.onNext(coinList.values.toList())
    }
}