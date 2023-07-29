package com.example.websocket_upbit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.websocket_upbit.data.model.Market

@Database(entities = [Market::class], version = 1, exportSchema = false)
abstract class MarketDatabase : RoomDatabase() {
    abstract fun marketDao(): MarketDao
}