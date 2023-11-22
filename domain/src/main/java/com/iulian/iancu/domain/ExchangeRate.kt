package com.iulian.iancu.domain

sealed class ExchangeRate {
    class USD(val value: Float): ExchangeRate()
    class GBP(val value: Float): ExchangeRate()
    class EUR(val value: Float): ExchangeRate()
}
