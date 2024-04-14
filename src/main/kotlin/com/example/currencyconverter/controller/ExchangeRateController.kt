package com.example.currencyconverter.controller

import com.example.currencyconverter.api.ExchangeRateResponse
import com.example.currencyconverter.service.CurrencyRateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ExchangeRateController(private val currencyRateService: CurrencyRateService) {

    @Operation(
        summary = "Get exchange rate for a given base currency",
        description = "Retrieves the exchange rates for the specified base currency from an external API."
    )
    @GetMapping("/exchange-rate")
    fun getExchangeRate(
        @Parameter(description = "Base currency code", example = "USD")
        @RequestParam baseCurrency: String,
    ): Mono<ExchangeRateResponse> {
        return currencyRateService.getExchangeRate(baseCurrency)
    }
}