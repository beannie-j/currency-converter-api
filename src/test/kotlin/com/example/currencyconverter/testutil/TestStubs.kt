package com.example.currencyconverter.testutil

import com.example.currencyconverter.api.ExchangeRateDetailsResponse
import com.example.currencyconverter.api.ExchangeRateResponse
import com.example.currencyconverter.exception.CurrencyRateException
import org.springframework.http.HttpStatus

object TestStubs {
    const val testBaseUrl: String = "https://test.example.com"
    const val usdCurrency = "USD"
    const val unknownCurrency = "UNKNOWN"

    val exchangeRateDetailsResponse = ExchangeRateDetailsResponse(
        result = "success",
        provider = "ExchangeRatesAPI",
        documentation = "https://www.exchangeratesapi.io/documentation/",
        termsOfUse = "https://www.exchangeratesapi.io/terms/",
        timeLastUpdateUnix = 1617954645L,
        timeLastUpdateUtc = "2021-04-09 17:10:45",
        timeNextUpdateUnix = 1617958245L,
        timeNextUpdateUtc = "2021-04-09 18:10:45",
        timeEolUnix = 1617961845L,
        baseCode = "USD",
        rates = mapOf(
            "EUR" to 0.842593,
            "GBP" to 0.72478,
            "JPY" to 109.656
        )
    )

    val exchangeRateResponse = ExchangeRateResponse(exchangeRateDetailsResponse)

    val currencyRateException = CurrencyRateException(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
    val currencyRateNotFoundException = CurrencyRateException("Currency not found")
}