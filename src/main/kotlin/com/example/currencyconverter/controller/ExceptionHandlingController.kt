package com.example.currencyconverter.controller

import com.example.currencyconverter.api.ApiError
import com.example.currencyconverter.exception.CurrencyRateClientException
import com.example.currencyconverter.exception.CurrencyRateServerException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlingController {

    @ExceptionHandler(CurrencyRateClientException::class)
    fun handleCurrencyRateClientException(ex: CurrencyRateClientException): ResponseEntity<ApiError> {
        val errorMessage = "Client error occurred: ${ex.message}"
        val apiError = ApiError(ex.statusCode, errorMessage)
        return buildResponseEntity(apiError)
    }

    @ExceptionHandler(CurrencyRateServerException::class)
    fun handleCurrencyRateServerException(ex: CurrencyRateServerException): ResponseEntity<ApiError> {
        val errorMessage = "An unexpected error occurred: ${ex.message}"
        val apiError = ApiError(ex.statusCode, errorMessage)
        return buildResponseEntity(apiError)
    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<ApiError> {
        return ResponseEntity(apiError, apiError.statusCode)
    }
}
