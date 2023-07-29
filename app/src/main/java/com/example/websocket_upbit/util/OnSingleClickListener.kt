package com.example.websocket_upbit.util

import android.view.View

interface OnSingleClickListener : View.OnClickListener {

    fun onSingleClick(v: View)
    fun onItemClick(v: View)

    companion object {
        const val CLICK_INTERVAL = 380L
    }

}