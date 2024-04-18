package com.example.currencyconverter.controller

import com.example.currencyconverter.service.CurrencyRateService
import com.example.currencyconverter.testutil.TestStubs.exchangeRateResponse
import com.example.currencyconverter.testutil.TestStubs.internalServerError
import com.example.currencyconverter.testutil.TestStubs.notFoundError
import com.example.currencyconverter.testutil.TestStubs.unknownCurrency
import com.example.currencyconverter.testutil.TestStubs.usdCurrency
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(ExchangeRateController::class)
class ExchangeRateControllerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var currencyRateService: CurrencyRateService

    @Test
    fun `should return exchange rate for a given base currency`() {
        `when`(currencyRateService.getExchangeRate(usdCurrency)).thenReturn(Mono.just(exchangeRateResponse))

        webTestClient.get()
            .uri("/exchange-rate?baseCurrency=$usdCurrency")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody().json("""
                {
                  "success": true,
                  "rates": {
                    "EUR": 0.842593,
                    "GBP": 0.72478,
                    "JPY": 109.656
                  },
                  "errorMessage": null
                }
            """.trimIndent())

        verify(currencyRateService).getExchangeRate(usdCurrency)
    }

    @Test
    fun `should return 400 Bad Request when base currency is invalid`() {
        `when`(currencyRateService.getExchangeRate(unknownCurrency))
            .thenReturn(Mono.error(notFoundError))

        webTestClient.get()
            .uri("/exchange-rate?baseCurrency=$unknownCurrency")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
            .expectBody().json("""
                {
                  "statusCode": "NOT_FOUND",
                  "message": "Client error occurred: Not Found"
                }
            """.trimIndent())

        verify(currencyRateService).getExchangeRate(unknownCurrency)
    }

    @Test
    fun `should return 500 Internal Server Error when an error occurs on the server side`() {
        `when`(currencyRateService.getExchangeRate(usdCurrency))
            .thenReturn(Mono.error(internalServerError))

        webTestClient.get()
            .uri("/exchange-rate?baseCurrency=$usdCurrency")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
            .expectBody().json("""
                {
                  "statusCode": "INTERNAL_SERVER_ERROR",
                  "message": "An unexpected error occurred: Internal Server Error"
                }
            """.trimIndent())

        verify(currencyRateService).getExchangeRate(usdCurrency)
    }

}
