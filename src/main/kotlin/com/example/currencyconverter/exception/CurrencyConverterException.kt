package com.example.currencyconverter.exception

open class CurrencyConverterException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class CurrencyRateException(message: String, cause: Throwable? = null) : CurrencyConverterException(message, cause)