package com.example.websocket_upbit.view.ui

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.websocket_upbit.R
import com.example.websocket_upbit.data.model.Ticker
import com.example.websocket_upbit.data.retrofit.ApiModule
import com.example.websocket_upbit.domain.repository.MarketRepository
import com.example.websocket_upbit.util.FLog
import com.example.websocket_upbit.view.base.BaseViewModel
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
) : BaseViewModel(application) {
    val tempText = MutableLiveData<Ticker>()

    init {
        title.set(getContext().getString(R.string.app_name))
        back.set(true)
    }

    fun getData() {
        loading.set(true)
        addDisposable(
            marketRepository.deleteAll().networkThread().andThen(
                ApiModule.startRetrofit().getMarket().networkThread(loading::set).toFlowable()
            )
                .filter { it.isNotEmpty() }
                .map { list ->
                    // KRW만
                    list.filter { market -> market.market.contains(getContext().getString(R.string.krw)) }
                }
                .flatMapCompletable {
                    FLog.e(it)
                    marketRepository.insert(it).networkThread()
                }
                .doFinally { loading.set(false) }
                .subscribe({ }, error::setValue)
        )
    }

    fun getTicker() {
        addDisposable(
            ApiModule.startRetrofit().getTicker("KRW-BTC")
                .networkThread(loading::set)
                .subscribe({
                    FLog.e(it)
                    onOpen()
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun onOpen() {
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
                FLog.d("onConnect")
                WebSocketManager.onMain()
            }

            override fun onClosed() {
                FLog.d("onClosed")
            }

            override fun onFailure() {
                // 연결 끊어진 경우 5초뒤 재 접속
                FLog.d("onFailure")
                addDisposable(
                    Observable.just(WebSocketManager).delay(5, TimeUnit.SECONDS)
                        .subscribe({ it.reconnect() }, error::setValue)
                )
            }

            override fun onData(data: Ticker) {
                // TICK 받음
                FLog.e(data)
                tempText.postValue(data)
            }
        }
        WebSocketManager.setListener(listener)
    }
}