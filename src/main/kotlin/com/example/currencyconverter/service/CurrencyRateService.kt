package com.example.currencyconverter.service

import com.example.currencyconverter.api.ExchangeRateDetailsResponse
import com.example.currencyconverter.api.ExchangeRateResponse
import com.example.currencyconverter.exception.CurrencyConverterException
import com.example.currencyconverter.exception.CurrencyRateException
import com.example.currencyconverter.exception.CurrencyRateNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Service
class CurrencyRateService(private val webClient: WebClient) {

    fun getExchangeRate(baseCurrency: String): Mono<ExchangeRateResponse> {

        val uri = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("open.er-api.com")
            .path("/v6/latest/$baseCurrency")
            .build()
            .toUri()

        return webClient.get()
            .uri(uri)
            .retrieve()
            .onStatus({ status -> status.is4xxClientError }, { clientResponse ->
                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                    val currencyRateNotFoundException = CurrencyRateNotFoundException("Currency not found")
                    return@onStatus Mono.error<CurrencyConverterException>(currencyRateNotFoundException)
                }
                Mono.error<CurrencyConverterException>(CurrencyRateException("Failed to fetch currency rates"))
            })
            .bodyToMono(ExchangeRateDetailsResponse::class.java)
            .map { ExchangeRateResponse(it) }
            .onErrorResume { it: Throwable -> Mono.just(ExchangeRateResponse(it)) }
    }
}