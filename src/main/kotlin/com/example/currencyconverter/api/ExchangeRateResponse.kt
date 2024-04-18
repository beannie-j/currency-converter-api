package com.example.currencyconverter.api

data class ExchangeRateResponse(
    val success: Boolean,
    val rates: Map<String, Double>,
    val errorMessage: String? = null
) {
    constructor(detailsResponse: ExchangeRateDetailsResponse) : this(
        success = !detailsResponse.rates.isNullOrEmpty(),
        rates = detailsResponse.rates ?: emptyMap(),
        errorMessage = detailsResponse.errorType
    )
}
