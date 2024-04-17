package com.example.currencyconverter.service

import com.example.currencyconverter.api.ExchangeRateResponse
import com.example.currencyconverter.testutil.TestStubs
import com.example.currencyconverter.testutil.TestUtil.readFileFromResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.net.URI

class CurrencyRateServiceTest {
    private lateinit var webClient: WebClient
    private lateinit var currencyRateService: CurrencyRateService

    private val exchangeFunction = mock(ExchangeFunction::class.java)

    @BeforeEach
    fun setUp() {
        webClient = WebClient.builder()
            .exchangeFunction(exchangeFunction)
            .build()

        currencyRateService = CurrencyRateService(webClient)
    }

    @Test
    fun `should return exchange rate response when HTTP request is successful`() {
        val successfulResponseJson = readFileFromResources("response.json")

        `when`(exchangeFunction.exchange(any())).thenReturn(
            Mono.just(
                ClientResponse.create(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(successfulResponseJson)
                    .build()
            )
        )

        val exchangeRate = currencyRateService.getExchangeRate("USD")

        StepVerifier.create(exchangeRate)
            .expectNext(ExchangeRateResponse(TestStubs.exchangeRateDetailsResponse))
            .verifyComplete()

        verify(exchangeFunction).exchange(argThat { request: ClientRequest ->
            request.url() == URI.create("https://open.er-api.com/v6/latest/USD")
                    && request.method() == HttpMethod.GET
        })
    }

    @Test
    fun `should throw CurrencyRateNotFoundException when currency is not found`() {
        val notFoundResponse = ClientResponse.create(HttpStatus.NOT_FOUND).build()

        `when`(exchangeFunction.exchange(any())).thenReturn(Mono.just(notFoundResponse))

        val exchangeRate = currencyRateService.getExchangeRate("UNKNOWN")

        StepVerifier.create(exchangeRate)
            .expectNext(ExchangeRateResponse(success = false, rates = emptyMap(), errorMessage = "Currency not found"))
            .verifyComplete()

        verify(exchangeFunction).exchange(argThat { request: ClientRequest ->
            println(request.url())
            println(request.method())
            request.url() == URI.create("https://open.er-api.com/v6/latest/UNKNOWN")
                    && request.method() == HttpMethod.GET
        })
    }

    @Test
    fun `should throw CurrencyRateException when HTTP request fails`() {
        val failedResponse = ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).build()

        `when`(exchangeFunction.exchange(any())).thenReturn(Mono.just(failedResponse))

        val exchangeRate = currencyRateService.getExchangeRate("USD")

        StepVerifier.create(exchangeRate)
            .expectNext(
                ExchangeRateResponse(
                    success = false,
                    rates = emptyMap(),
                    errorMessage = "500 Internal Server Error from UNKNOWN "
                )
            )
            .verifyComplete()

        verify(exchangeFunction).exchange(argThat { request: ClientRequest ->
            request.url() == URI.create("https://open.er-api.com/v6/latest/USD")
                    && request.method() == HttpMethod.GET
        })
    }

}