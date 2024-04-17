package com.example.currencyconverter.controller

import com.example.currencyconverter.api.ExchangeRateResponse
import com.example.currencyconverter.exception.CurrencyRateException
import com.example.currencyconverter.exception.CurrencyRateNotFoundException
import com.example.currencyconverter.service.CurrencyRateService
import com.example.currencyconverter.testutil.TestStubs.exchangeRateResponse
import com.example.currencyconverter.testutil.TestStubs.unknownCurrency
import com.example.currencyconverter.testutil.TestStubs.usdCurrency
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ExchangeRateControllerTest {
    private lateinit var currencyRateService: CurrencyRateService
    private lateinit var exchangeRateController: ExchangeRateController

    @BeforeEach
    fun setUp() {
        currencyRateService = mock(CurrencyRateService::class.java)
        exchangeRateController = ExchangeRateController(currencyRateService)
    }

    @Test
    fun `should return exchange rate when HTTP status is 200`() {
        `when`(currencyRateService.getExchangeRate(anyString()))
            .thenReturn(Mono.just(exchangeRateResponse))

        StepVerifier.create(exchangeRateController.getExchangeRate(usdCurrency))
            .expectNext(exchangeRateResponse)
            .verifyComplete()

        verify(currencyRateService).getExchangeRate(usdCurrency)
    }

    @Test
    fun `should throw CurrencyRateNotFoundException when HTTP status is 404`() {
        val currencyNotFoundResponse = ExchangeRateResponse(CurrencyRateNotFoundException("Currency not found"))

        `when`(currencyRateService.getExchangeRate(anyString()))
            .thenReturn(Mono.just(currencyNotFoundResponse))

        StepVerifier.create(exchangeRateController.getExchangeRate(unknownCurrency))
            .expectNext(currencyNotFoundResponse)
            .verifyComplete()

        verify(currencyRateService).getExchangeRate(unknownCurrency)
    }

    @Test
    fun `should return empty response when HTTP status is 500`() {
        val internalServerErrorResponse =
            ExchangeRateResponse(CurrencyRateException(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase))

        `when`(currencyRateService.getExchangeRate(anyString()))
            .thenReturn(Mono.just(internalServerErrorResponse))

        StepVerifier.create(exchangeRateController.getExchangeRate(usdCurrency))
            .expectNext(internalServerErrorResponse)
            .verifyComplete()

        verify(currencyRateService).getExchangeRate(usdCurrency)
    }
}