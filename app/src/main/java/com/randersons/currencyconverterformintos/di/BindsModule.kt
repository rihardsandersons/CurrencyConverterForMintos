package com.randersons.currencyconverterformintos.di

import com.randersons.currencyconverterformintos.data.FrankfurterRepositoryImpl
import com.randersons.currencyconverterformintos.domain.FrankfurterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    @Singleton
    abstract fun bindFrankfurterRepository(
        frankfurterRepositoryImpl: FrankfurterRepositoryImpl
    ): FrankfurterRepository

}
