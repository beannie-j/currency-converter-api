package com.example.currencyconverter.service

import com.example.currencyconverter.api.ExchangeRateDetailsResponse
import com.example.currencyconverter.api.ExchangeRateResponse
import com.example.currencyconverter.exception.CurrencyConverterException
import com.example.currencyconverter.exception.CurrencyRateException
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI

@Service
class CurrencyRateService(private val webClient: WebClient) {

    fun getExchangeRate(baseCurrency: String): Mono<ExchangeRateResponse> {

        val uri: URI = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("open.er-api.com")
            .path("/v6/latest/$baseCurrency")
            .build()
            .toUri()

        return webClient.get()
            .uri(uri)
            .retrieve()
            .onStatus({ status -> !status.is2xxSuccessful }, { clientResponse ->
                val errorMessage = generateErrorMessage(clientResponse.statusCode())
                Mono.error<CurrencyConverterException>(CurrencyRateException(errorMessage))
            })
            .bodyToMono(ExchangeRateDetailsResponse::class.java)
            .map { ExchangeRateResponse(it) }
    }

    private fun generateErrorMessage(httpStatusCode: HttpStatusCode): String {
        return when {
            httpStatusCode.is4xxClientError -> "Failed to fetch currency rates: ${httpStatusCode.value()} Client Error"
            httpStatusCode.is5xxServerError -> "Failed to fetch currency rates: ${httpStatusCode.value()} Server Error"
            else -> "Failed to fetch currency rates: ${httpStatusCode.value()} Error"
        }
    }
}