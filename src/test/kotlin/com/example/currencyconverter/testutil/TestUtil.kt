package com.example.currencyconverter.testutil

import java.nio.charset.StandardCharsets

object TestUtil {
    fun readFileFromResources(fileName: String): String {
        return this::class.java.classLoader.getResourceAsStream("$fileName")
            ?.bufferedReader(StandardCharsets.UTF_8)
            ?.use { it.readText() } ?: throw IllegalArgumentException("File not found: $this")
    }
}