package com.iulian.iancu.cryptoscreenapp

import androidx.lifecycle.ViewModel
import com.iulian.iancu.domain.GetCurrencyRateUseCase
import com.iulian.iancu.domain.GetUserWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserWalletUseCase: GetUserWalletUseCase,
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase
) : ViewModel(),IMainViewModel {

}


interface IMainViewModel

object FakeViewModel: IMainViewModel {

}