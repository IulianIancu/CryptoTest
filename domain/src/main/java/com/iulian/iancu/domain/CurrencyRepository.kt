package com.iulian.iancu.domain

interface CurrencyRepository {
    suspend fun getCurrencies():  Either<Throwable,List<ExchangeRate>>
}