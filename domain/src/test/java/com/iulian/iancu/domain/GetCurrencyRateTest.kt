package com.iulian.iancu.domain

import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetCurrencyRateTest {

    @Test
    fun currency_list_comes_trough_use_case() = runTest {
        val currencyRepository = CurrencyRepositoryTestImpl()
        val getCurrencyRateUseCase = GetCurrencyRateUseCase(currencyRepository)

        val result = getCurrencyRateUseCase() as Either.Success

        Assert.assertEquals(3, result.value.size)
        Assert.assertEquals(10f, (result.value.first() as ExchangeRate.EUR).value)
        Assert.assertEquals(10f, (result.value[1] as ExchangeRate.GBP).value)
        Assert.assertEquals(10f, (result.value[2] as ExchangeRate.USD).value)
    }
}