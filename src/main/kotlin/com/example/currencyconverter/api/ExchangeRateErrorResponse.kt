package com.example.currencyconverter.api

import com.fasterxml.jackson.annotation.JsonProperty

data class ExchangeRateErrorResponse(
    @JsonProperty("result")
    val result: String,
    @JsonProperty("error-type")
    val errorType: String
)
