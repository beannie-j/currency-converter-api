package com.example.currencyconverter.exception

import org.springframework.http.HttpStatusCode

open class CurrencyConverterException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class CurrencyRateServerException(
    message: String,
    val statusCode: HttpStatusCode,
    cause: Throwable? = null
) : CurrencyConverterException(message)

class CurrencyRateClientException(
    message: String,
    val statusCode: HttpStatusCode,
    cause: Throwable? = null
) : CurrencyConverterException(message)