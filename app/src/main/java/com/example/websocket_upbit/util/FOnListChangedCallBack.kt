package com.example.websocket_upbit.util

import androidx.databinding.ObservableList

abstract class FOnListChangedCallBack<T : ObservableList<*>> : ObservableList.OnListChangedCallback<T>() {
    override fun onChanged(sender: T) {

    }

    override fun onItemRangeRemoved(sender: T, positionStart: Int, itemCount: Int) {

    }

    override fun onItemRangeMoved(sender: T, fromPosition: Int, toPosition: Int, itemCount: Int) {

    }

    override fun onItemRangeInserted(sender: T, positionStart: Int, itemCount: Int) {

    }

    override fun onItemRangeChanged(sender: T, positionStart: Int, itemCount: Int) {}
}