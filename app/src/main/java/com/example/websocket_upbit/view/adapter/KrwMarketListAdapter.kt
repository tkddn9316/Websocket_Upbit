package com.example.websocket_upbit.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_upbit.data.model.Ticker
import com.example.websocket_upbit.databinding.ItemKrwMarketBinding
import com.example.websocket_upbit.view.base.BaseListAdapter

class KrwMarketListAdapter(
    context: Context,
    val onListener: View.OnClickListener
) : BaseListAdapter<KrwMarketListAdapter.ItemViewHolder, Ticker>(context) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KrwMarketListAdapter.ItemViewHolder {
        return ItemViewHolder(
            ItemKrwMarketBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: KrwMarketListAdapter.ItemViewHolder, position: Int) {
        with(holder) {
            bind(getItem(position))
        }
    }

    inner class ItemViewHolder(val binding: ItemKrwMarketBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layout.setOnClickListener(onListener)
        }

        fun bind(data: Ticker) {
            binding.apply {
                model = data
                root.tag = data
                layout.tag = data

                executePendingBindings()
            }
        }
    }
}