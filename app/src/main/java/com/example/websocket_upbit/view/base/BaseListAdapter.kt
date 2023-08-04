package com.example.websocket_upbit.view.base

import android.content.Context
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_upbit.data.model.Ticker

// TODO: Model은 임시
abstract class BaseListAdapter<VH : RecyclerView.ViewHolder, M : Ticker>(
    val context: Context
) : RecyclerView.Adapter<VH>() {
    private var data = mutableListOf<M>()

    fun getData(): MutableList<M> {
        return data
    }

    open fun setData(items: List<M>) {
        data.clear().also { data.addAll(items) }
        notifyDataSetChanged()
    }

    fun getItem(index: Int): M {
        return data[index]
    }

    fun addData(index: Int, item: M) {
        val temp = data
        temp.add(index, item)
        setData(temp)
    }

    fun addData(item: M) {
        val temp = data
        temp.add(item)
        setData(temp)
    }

    fun addToFront(listItems: List<M>) {
        val temp = data
        temp.addAll(0, listItems)
        setData(temp)
    }

    fun updateData(index: Int, listItems: M) {
        val temp = data
        temp.removeAt(index)
        temp.add(index, listItems)
        setData(temp)
    }

    fun addToBottom(listItems: List<M>) {
        val temp = data
        temp.addAll(listItems)
        setData(temp)
    }

    override fun getItemCount(): Int {
        return if (data.isNullOrEmpty()) {
            0
        } else {
            data.size
        }
    }

    fun clear() {
        if (data.isNullOrEmpty()) {
            data.clear().also { notifyDataSetChanged() }
        }
    }

    open fun removeData(position: Int) {
        data.removeAt(position).also { notifyItemRemoved(position) }
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }
}