package com.example.currencyconverter.service

import com.example.currencyconverter.api.ExchangeRateDetailsResponse
import com.example.currencyconverter.api.ExchangeRateResponse
import com.example.currencyconverter.exception.CurrencyConverterException
import com.example.currencyconverter.exception.CurrencyRateException
import com.example.currencyconverter.exception.CurrencyRateNotFoundException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Service
class CurrencyRateService(private val webClient: WebClient) {
    companion object {
        private val exchangeRateDetailsResponseType =
            object : ParameterizedTypeReference<ExchangeRateDetailsResponse>() {}
    }

    fun getExchangeRate(baseCurrency: String): Mono<ExchangeRateResponse> {
        //GET https://open.er-api.com/v6/latest/USD

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
            .bodyToMono(exchangeRateDetailsResponseType)
            .map { ExchangeRateResponse(it) }
            .onErrorResume { it: Throwable -> Mono.just(ExchangeRateResponse(it)) }
    }
}