package com.example.websocket_upbit.domain.di

import com.example.websocket_upbit.data.db.MarketDataSource
import com.example.websocket_upbit.domain.repository.MarketRepository
import com.example.websocket_upbit.domain.repository.MarketRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMarketRepository(marketDataSource: MarketDataSource): MarketRepository {
        return MarketRepositoryImpl(marketDataSource)
    }
}