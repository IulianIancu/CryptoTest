package com.iulian.iancu.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @Expose
    @SerializedName("ethereum")
    val ethereum: Ethereum
)

data class Ethereum(
    @Expose
    @SerializedName("usd")
    val usd: Float?,
    @Expose
    @SerializedName("eur")
    val eur: Float?,
    @Expose
    @SerializedName("gbp")
    val gbp: Float?,
)
