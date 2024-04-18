package com.example.currencyconverter.controller

import com.example.currencyconverter.exception.CurrencyRateClientException
import com.example.currencyconverter.exception.CurrencyRateServerException
import com.example.currencyconverter.service.CurrencyRateService
import com.example.currencyconverter.testutil.TestStubs.exchangeRateResponse
import com.example.currencyconverter.testutil.TestStubs.internalServerError
import com.example.currencyconverter.testutil.TestStubs.notFoundError
import com.example.currencyconverter.testutil.TestStubs.unknownCurrency
import com.example.currencyconverter.testutil.TestStubs.usdCurrency
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
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
        `when`(currencyRateService.getExchangeRate(anyString()))
            .thenReturn(Mono.error(notFoundError))

        StepVerifier.create(exchangeRateController.getExchangeRate(unknownCurrency))
            .expectErrorSatisfies {
                assertThat(it).isInstanceOf(CurrencyRateClientException::class.java)
                assertThat(it.message).isEqualTo("Not Found")
            }
            .verify()

        verify(currencyRateService).getExchangeRate(unknownCurrency)
    }

    @Test
    fun `should return empty response when HTTP status is 500`() {
        `when`(currencyRateService.getExchangeRate(anyString()))
            .thenReturn(Mono.error(internalServerError))

        StepVerifier.create(exchangeRateController.getExchangeRate(usdCurrency))
            .expectErrorSatisfies {
                assertThat(it).isInstanceOf(CurrencyRateServerException::class.java)
                assertThat(it.message).isEqualTo("Internal Server Error")
            }
            .verify()

        verify(currencyRateService).getExchangeRate(usdCurrency)
    }
}