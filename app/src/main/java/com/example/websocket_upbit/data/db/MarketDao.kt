package com.example.websocket_upbit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.websocket_upbit.data.model.Market
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface MarketDao {
    @Query("SELECT * FROM Market")
    fun getAll(): Single<List<Market>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: List<Market>): Completable

    @Query("DELETE FROM Market")
    fun deleteAll(): Completable
}