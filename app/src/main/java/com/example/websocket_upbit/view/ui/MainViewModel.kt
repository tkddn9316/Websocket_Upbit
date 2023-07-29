package com.example.websocket_upbit.view.ui

import android.app.Application
import androidx.databinding.ObservableField
import com.example.websocket_upbit.R
import com.example.websocket_upbit.data.retrofit.ApiModule
import com.example.websocket_upbit.domain.repository.MarketRepository
import com.example.websocket_upbit.util.FLog
import com.example.websocket_upbit.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val marketRepository: MarketRepository,
    application: Application
) : BaseViewModel(application) {
    val text = ObservableField("")

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
                }, {
                    it.printStackTrace()
                })
        )
    }
}