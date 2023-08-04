package com.example.websocket_upbit.view.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_upbit.R
import com.example.websocket_upbit.data.model.Ticker
import com.example.websocket_upbit.databinding.ActivityMainBinding
import com.example.websocket_upbit.view.adapter.KrwMarketListAdapter
import com.example.websocket_upbit.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_main)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
//        binding.recyclerView.onClear()
        viewModel.getData(0)
    }

    override fun getAdapter(): RecyclerView.Adapter<*> {
        return KrwMarketListAdapter(context, this::onItemClick)
    }

    override fun onItemClick(v: View) {
        super.onItemClick(v)
        Toast.makeText(context, (v.tag as Ticker).korean_name, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onBind()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onClosed()
    }
}