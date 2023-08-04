package com.example.websocket_upbit.view.base

import androidx.databinding.ObservableArrayList

interface FObservable<T> {
    fun getObservableArrayList(): ObservableArrayList<*>
    fun getData(any: Any?)
    fun onData(data: T)
    fun submit(any: Any?)
    fun onComplete()
    fun onClear()
}