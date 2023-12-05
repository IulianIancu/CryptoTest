package com.iulian.iancu.cryptoscreenapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iulian.iancu.domain.Either
import com.iulian.iancu.domain.ExchangeRate
import com.iulian.iancu.domain.GetCurrencyRateUseCase
import com.iulian.iancu.domain.GetUserWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserWalletUseCase: GetUserWalletUseCase,
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase
) : ViewModel(), IMainViewModel {
    private var currentWalletLimit = 0f
    private var _currencyRateList = MutableStateFlow<List<ExchangeRate>>(emptyList())
    override val currencyRateList: StateFlow<List<ExchangeRate>> = _currencyRateList

    private val _currentTransferValueInCash = MutableStateFlow(0f)
    override val currentTransferValueInCash = _currentTransferValueInCash.asStateFlow()

    private val _currentTransferValueInETH = MutableStateFlow(0f)
    override val currentTransferValueInEth = _currentTransferValueInETH.asStateFlow()

    private val _isTransferInCash = MutableStateFlow(true)
    override val isTransferInCash: StateFlow<Boolean> = _isTransferInCash.asStateFlow()

    private val _currentExchangeRate = MutableStateFlow<ExchangeRate>(ExchangeRate.EUR(1f))
    override val currentExchangeRate: StateFlow<ExchangeRate> = _currentExchangeRate.asStateFlow()

    private val _isCurrencySelectorOpen = MutableStateFlow(false)
    override val isCurrencySelectorOpen: StateFlow<Boolean> = _isCurrencySelectorOpen.asStateFlow()

    init {
        currentWalletLimit = getUserWalletUseCase()

        viewModelScope.launch {
            val exchangeRateResult = getCurrencyRateUseCase()
            if (exchangeRateResult is Either.Success) {
                _currencyRateList.value = exchangeRateResult.value
                _currentExchangeRate.value =
                    _currencyRateList.value.first { it is ExchangeRate.EUR }
            } else {
                //TODO bad UX, give the user some feedback
                Timber.e(exchangeRateResult.toString())
            }
        }
    }

    override fun setNewValueCash(cash: Float) {
        _currentTransferValueInCash.value = cash
        when (val rate = currentExchangeRate.value) {
            is ExchangeRate.EUR -> _currentTransferValueInETH.value = cash / rate.value
            is ExchangeRate.GBP -> _currentTransferValueInETH.value = cash / rate.value
            is ExchangeRate.USD -> _currentTransferValueInETH.value = cash / rate.value
        }
    }

    override fun setNewValueETH(eth: Float) {
        _currentTransferValueInETH.value = eth
        when (val rate = currentExchangeRate.value) {
            is ExchangeRate.EUR -> _currentTransferValueInCash.value = eth * rate.value
            is ExchangeRate.GBP -> _currentTransferValueInCash.value = eth * rate.value
            is ExchangeRate.USD -> _currentTransferValueInCash.value = eth * rate.value
        }

    }

    override fun swapTransferMode() {
        _isTransferInCash.value = !isTransferInCash.value
    }

    override fun setIsCurrencySelectorOpen(isOpen: Boolean) {
        _isCurrencySelectorOpen.value = isOpen
    }

    override fun setCurrentExchangeRate(rate: ExchangeRate) {
        _currentExchangeRate.value = rate
        val eth =_currentTransferValueInETH.value
        when (val rate = currentExchangeRate.value) {
            is ExchangeRate.EUR -> _currentTransferValueInCash.value = eth * rate.value
            is ExchangeRate.GBP -> _currentTransferValueInCash.value = eth * rate.value
            is ExchangeRate.USD -> _currentTransferValueInCash.value = eth * rate.value
        }
    }
}


interface IMainViewModel {
    val currentTransferValueInCash: StateFlow<Float>
    val currentTransferValueInEth: StateFlow<Float>
    val isTransferInCash: StateFlow<Boolean>
    val currentExchangeRate: StateFlow<ExchangeRate>
    val isCurrencySelectorOpen: StateFlow<Boolean>
    val currencyRateList: StateFlow<List<ExchangeRate>>

    fun setNewValueCash(cash: Float)
    fun setNewValueETH(eth: Float)
    fun swapTransferMode()
    fun setIsCurrencySelectorOpen(isOpen: Boolean)
    fun setCurrentExchangeRate(rate: ExchangeRate)
}

object FakeViewModel : IMainViewModel {
    override val currentTransferValueInCash: StateFlow<Float> = MutableStateFlow(0f)
    override val currentTransferValueInEth: StateFlow<Float> = MutableStateFlow(0f)
    override val isTransferInCash: StateFlow<Boolean> = MutableStateFlow(true)
    override val currentExchangeRate: StateFlow<ExchangeRate> =
        MutableStateFlow(ExchangeRate.EUR(1f))
    override val isCurrencySelectorOpen: StateFlow<Boolean> = MutableStateFlow(false)
    override val currencyRateList: StateFlow<List<ExchangeRate>> = MutableStateFlow(emptyList())

    override fun setNewValueCash(cash: Float) {}
    override fun setNewValueETH(eth: Float) {}
    override fun swapTransferMode() {}
    override fun setIsCurrencySelectorOpen(isOpen: Boolean) {}
    override fun setCurrentExchangeRate(rate: ExchangeRate) {}

}