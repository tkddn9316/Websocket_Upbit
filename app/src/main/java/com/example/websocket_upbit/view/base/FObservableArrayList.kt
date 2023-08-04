package com.example.websocket_upbit.view.base

import androidx.databinding.ListChangeRegistry
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList

class FObservableArrayList<T> : ObservableArrayList<T>() {
    @Transient
    val listeners = ListChangeRegistry()

    override fun addAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty()) listeners.notifyInserted(this, 0, 0)
        return super.addAll(elements)
    }

    override fun addOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<*>>?) {
        listeners.add(listener)
        super.addOnListChangedCallback(listener)
    }

    override fun removeOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<*>>?) {
        listeners.remove(listener)
        super.removeOnListChangedCallback(listener)
    }
}