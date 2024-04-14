package com.example.currencyconverter.controller

import com.example.currencyconverter.api.ExchangeRateResponse
import com.example.currencyconverter.service.CurrencyRateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ExchangeRateController(private val currencyRateService: CurrencyRateService) {

    @GetMapping("/exchange-rate")
    fun getExchangeRate(
        @RequestParam baseCurrency: String,
    ): Mono<ExchangeRateResponse> {
        return currencyRateService.getExchangeRate(baseCurrency)
    }
}