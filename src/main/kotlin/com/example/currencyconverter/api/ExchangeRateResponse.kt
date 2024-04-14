package com.example.currencyconverter.api

import java.util.Collections.emptyMap

data class ExchangeRateResponse(
    val success: Boolean,
    val rates: Map<String, Double>,
    val errorMessage: String? = null
) {
    constructor(detailsResponse: ExchangeRateDetailsResponse) : this(
        success = true,
        rates = detailsResponse.rates,
        errorMessage = null
    )

    constructor(exception: Throwable) : this(
        success = false,
        rates = emptyMap(),
        errorMessage = exception.message
    )
}
