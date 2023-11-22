package com.iulian.iancu.domain

import org.junit.Test

import org.junit.Assert.*

class GetUserWalletTest {
    @Test
    fun wallet_is_exactly_10() {
        assertEquals(10f, GetUserWalletUseCase().invoke())
    }
}