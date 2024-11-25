package com.randersons.currencyconverterformintos.di

import com.randersons.currencyconverterformintos.data.remote.FrankfurterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProviderModuleSingleton {

    @Provides
    @Singleton
    fun provideFrankfurterApi(): FrankfurterApi {
        return Retrofit.Builder()
            .baseUrl(FrankfurterApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FrankfurterApi::class.java)
    }
}
