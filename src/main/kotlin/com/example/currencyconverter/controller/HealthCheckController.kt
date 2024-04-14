package com.example.currencyconverter.controller

import org.springframework.boot.actuate.health.Health
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @GetMapping("/")
    fun healthCheck(): Health? {
        return Health.up().build()
    }
}