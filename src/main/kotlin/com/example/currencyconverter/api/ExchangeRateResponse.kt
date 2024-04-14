package com.example.currencyconverter.api

import com.fasterxml.jackson.annotation.JsonProperty

data class ExchangeRateResponse(
    @JsonProperty("result")
    val result: String,
    @JsonProperty("provider")
    val provider: String,
    @JsonProperty("documentation")
    val documentation: String,
    @JsonProperty("terms_of_use")
    val termsOfUse: String,
    @JsonProperty("time_last_update_unix")
    val timeLastUpdateUnix: Long,
    @JsonProperty("time_last_update_utc")
    val timeLastUpdateUtc: String,
    @JsonProperty("time_next_update_unix")
    val timeNextUpdateUnix: Long,
    @JsonProperty("time_next_update_utc")
    val timeNextUpdateUtc: String,
    @JsonProperty("time_eol_unix")
    val timeEolUnix: Long,
    @JsonProperty("base_code")
    val baseCode: String,
    @JsonProperty("rates")
    val rates: Map<String, Double>
)
