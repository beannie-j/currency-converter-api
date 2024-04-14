package com.example.currencyconverter.service

import com.example.currencyconverter.api.ExchangeRateResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Service
class CurrencyRateService(private val webClient: WebClient) {
    companion object {
        private val exchangeRateResponseType = object : ParameterizedTypeReference<ExchangeRateResponse>() {}
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
            .bodyToMono(exchangeRateResponseType)
    }
}