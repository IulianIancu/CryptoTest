package com.iulian.iancu.cryptoscreenapp.di

import com.iulian.iancu.data.CurrencyRepositoryImpl
import com.iulian.iancu.data.ExchangeRateService
import com.iulian.iancu.domain.CurrencyRepository
import com.iulian.iancu.domain.GetCurrencyRateUseCase
import com.iulian.iancu.domain.GetUserWalletUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class MainModule {

    @Provides
    fun provideGetUserWalletUseCase(): GetUserWalletUseCase = GetUserWalletUseCase()

    @Provides
    fun provideGetCurrencyRateUseCase(repository: CurrencyRepository): GetCurrencyRateUseCase =
        GetCurrencyRateUseCase(repository)

    @Provides
    fun provideCurrencyRepository(service: ExchangeRateService): CurrencyRepository =
        CurrencyRepositoryImpl(service)

    @Provides
    fun provideExchangeRateService(): ExchangeRateService = ExchangeRateService.getInstance()
}