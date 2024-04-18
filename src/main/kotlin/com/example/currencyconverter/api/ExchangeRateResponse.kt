package com.example.currencyconverter.api

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
}
