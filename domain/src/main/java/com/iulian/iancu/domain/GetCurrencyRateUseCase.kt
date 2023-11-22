package com.iulian.iancu.domain

class GetCurrencyRateUseCase(private val currencyRepository: CurrencyRepository) {

    private suspend fun run(): Either<Throwable, List<ExchangeRate>> {
        return currencyRepository.getCurrencies()
    }

    suspend operator fun invoke(): Either<Throwable, List<ExchangeRate>> {
        return run()
    }
}