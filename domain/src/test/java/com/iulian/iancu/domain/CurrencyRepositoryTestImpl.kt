package com.iulian.iancu.domain

class CurrencyRepositoryTestImpl : CurrencyRepository {
    override suspend fun getCurrencies(): Either<Throwable, List<ExchangeRate>> {
        return Either.success(
            listOf(
                ExchangeRate.EUR(10f),
                ExchangeRate.GBP(10f),
                ExchangeRate.USD(10f)
            )
        )
    }
}