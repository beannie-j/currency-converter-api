package com.example.currencyconverter.api

import org.springframework.http.HttpStatusCode

data class ApiError(
    val statusCode: HttpStatusCode,
    val message: String,
)