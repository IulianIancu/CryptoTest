package com.iulian.iancu.data

import com.iulian.iancu.domain.CurrencyRepository
import com.iulian.iancu.domain.Either
import com.iulian.iancu.domain.ExchangeRate
import java.util.ServiceConfigurationError

class CurrencyRepositoryImpl(private val exchangeRateService: ExchangeRateService) : CurrencyRepository {
    private var USD_rate: Float? = null
    private var EUR_rate: Float? = null
    private var GBP_rate: Float? = null

    override suspend fun getCurrencies(): Either<Throwable, List<ExchangeRate>> {
        val givenList = mutableListOf<ExchangeRate>()

        //Check USD rate exists in active memory, if not grab it from the server
        if (USD_rate == null) {
            val result = exchangeRateService.getExchangeRates(currency = "usd")
            if (result.isSuccessful) {
                val rate = result.body()?.ethereum?.usd
                if (rate != null) {
                    givenList.add(ExchangeRate.USD(rate))
                    USD_rate = rate
                } else return Either.fail(UnknownError("Response says success but no USD?"))
            } else return Either.fail(ServiceConfigurationError(result.errorBody()?.string()))
        } else givenList.add(
            ExchangeRate.USD(USD_rate ?: return Either.fail(UnknownError("Where did USD go?")))
        )


        //Check EUR rate exists in active memory, if not grab it from the server
        if (EUR_rate == null) {
            val result = exchangeRateService.getExchangeRates(currency = "eur")
            if (result.isSuccessful) {
                val rate = result.body()?.ethereum?.eur
                if (rate != null) {
                    givenList.add(ExchangeRate.USD(rate))
                    EUR_rate = rate
                } else return Either.fail(UnknownError("Response says success but no EUR?"))
            } else return Either.fail(ServiceConfigurationError(result.errorBody()?.string()))
        } else givenList.add(
            ExchangeRate.USD(EUR_rate ?: return Either.fail(UnknownError("Where did EUR go?")))
        )

        //Check GBP rate exists in active memory, if not grab it from the server
        if (GBP_rate == null) {
            val result = exchangeRateService.getExchangeRates(currency = "gbp")
            if (result.isSuccessful) {
                val rate = result.body()?.ethereum?.gbp
                if (rate != null) {
                    givenList.add(ExchangeRate.USD(rate))
                    GBP_rate = rate
                } else return Either.fail(UnknownError("Response says success but no GBP?"))
            } else return Either.fail(ServiceConfigurationError(result.errorBody()?.string()))
        } else givenList.add(
            ExchangeRate.USD(GBP_rate ?: return Either.fail(UnknownError("Where did GBP go?")))
        )

        return Either.success(givenList)
    }
}