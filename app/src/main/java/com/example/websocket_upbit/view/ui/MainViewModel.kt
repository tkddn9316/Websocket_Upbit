package com.example.websocket_upbit.view.ui

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.example.websocket_upbit.R
import com.example.websocket_upbit.data.model.Market
import com.example.websocket_upbit.data.model.Ticker
import com.example.websocket_upbit.data.retrofit.ApiModule
import com.example.websocket_upbit.data.retrofit.networkThread
import com.example.websocket_upbit.domain.repository.MarketRepository
import com.example.websocket_upbit.util.FLog
import com.example.websocket_upbit.view.base.BaseViewModel
import com.example.websocket_upbit.view.base.FObservable
import com.example.websocket_upbit.view.base.FObservableArrayList
import com.example.websocket_upbit.websocket.WebSocketDataListener
import com.example.websocket_upbit.websocket.WebSocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val marketRepository: MarketRepository,
    application: Application
) : BaseViewModel(application), FObservable<List<Ticker>> {
    private val marketNameList = mutableListOf<Market>()
    val items: ObservableArrayList<Ticker> = FObservableArrayList()

    init {
        title.set(getContext().getString(R.string.app_name))
        back.set(true)

        addDisposable(
            marketRepository.getTickEventPublisher()
                .subscribe({ item ->
                    val index = items.indexOf(items.find { it.market == item.code })
                    items[index] = item
                }, { })
        )
    }

    override fun getObservableArrayList(): ObservableArrayList<*> {
        return items
    }

    override fun getData(any: Any?) {
        any?.let { page ->
            if (page is Int) {
                loading.set(true)
                addDisposable(
                    marketRepository.deleteAll().networkThread().andThen(
                        // 마켓 코드가 먼저 필요하므로 getMarket
                        ApiModule.startRetrofit().getMarket().networkThread(loading::set)
                            .toFlowable()
                    )
                        .filter { it.isNotEmpty() }
                        .map { list ->
                            // KRW만
                            list.filter { market -> market.market.contains(getContext().getString(R.string.krw)) }
                        }
                        .map { list ->
                            // 30개만
                            if (list.size >= 30) list.subList(0, 29) else list
                        }
                        .flatMap {
                            FLog.e(it)
                            marketNameList.addAll(it)
                            val marketCodeList = it.map { market -> market.market }
                            // DB 저장
                            marketRepository.insert(it).networkThread().andThen(
                                // 최초 TICKER 생성
                                ApiModule.startRetrofit().getTickers(marketCodeList)
                                    .networkThread(loading::set)
                                    .toFlowable()
                            )
                        }
                        .doFinally { loading.set(false) }
                        .subscribe(this::onData, error::setValue)
                )
            }
        }
    }

    override fun onData(data: List<Ticker>) {
        val list = data.onEach { ticker ->
            marketNameList.find { market -> market.market == ticker.market }?.let { market ->
                // TICKER에 한글 이름이 없어서 넣어야함
                ticker.korean_name = market.korean_name
            }
        }
        FLog.e(list)
        items.addAll(list.sortedByDescending { ticker -> ticker.trade_price })
        onOpen()
    }

    override fun submit(any: Any?) {

    }

    override fun onComplete() {

    }

    override fun onClear() {
        items.clear()
        getData(1)
    }

    private fun onOpen() {
        onClosed()
        WebSocketManager.onOpen()
    }

    fun onClosed() {
        FLog.d("onClosed")
        WebSocketManager.onClose()
    }

    fun onBind() {
        val listener = object : WebSocketDataListener {
            override fun onConnect() {
                FLog.e("onConnect")
                addDisposable(
                    marketRepository.getAll().networkThread()
                        .subscribe({ WebSocketManager.onMain(it) }, { })
                )
            }

            override fun onClosed() {
                FLog.e("onClosed")
            }

            override fun onFailure() {
                // 연결 끊어진 경우 5초 뒤 재접속
                FLog.e("onFailure")
                addDisposable(
                    Observable.just(WebSocketManager).delay(5, TimeUnit.SECONDS)
                        .subscribe({ it.reconnect() }, error::setValue)
                )
            }

            override fun onData(data: Ticker) {
                // TICK 받음
//                FLog.e(data)
                marketNameList.find { market -> market.market == data.code }?.let { market ->
                    data.korean_name = market.korean_name
                }
                marketRepository.updateTick(data)
            }
        }
        WebSocketManager.setListener(listener)
    }
}