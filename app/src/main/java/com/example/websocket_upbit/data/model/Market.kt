package com.example.websocket_upbit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Market(
    @PrimaryKey(autoGenerate = true)
    var _index: Long,
    var market: String,
    var korean_name: String,
    var english_name: String
)