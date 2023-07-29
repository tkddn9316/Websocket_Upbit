package com.example.websocket_upbit.domain.di

import android.content.Context
import androidx.room.Room
import com.example.websocket_upbit.data.db.MarketDao
import com.example.websocket_upbit.data.db.MarketDataSource
import com.example.websocket_upbit.data.db.MarketDataSourceImpl
import com.example.websocket_upbit.data.db.MarketDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvideDatabaseModule {
    /** Provide Room Database */
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MarketDatabase {
        return Room.databaseBuilder(
            context, MarketDatabase::class.java, "example.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMarketDataSource(marketDao: MarketDao): MarketDataSource {
        return MarketDataSourceImpl(marketDao)
    }

    @Singleton
    @Provides
    fun provideMarketDao(db: MarketDatabase): MarketDao = db.marketDao()
}