package com.example.websocket_upbit.view.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.example.websocket_upbit.R
import com.example.websocket_upbit.databinding.ActivityMainBinding
import com.example.websocket_upbit.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_main)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        viewModel.getData()
        viewModel.getTicker()
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