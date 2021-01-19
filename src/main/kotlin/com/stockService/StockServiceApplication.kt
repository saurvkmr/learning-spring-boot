package com.stockService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StockServiceApplication

fun main(args: Array<String>) {
	runApplication<StockServiceApplication>(*args)
}
