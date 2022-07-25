package com.example.halooglasi_telegram_bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class HalooglasiTelegramBotApplication

fun main(args: Array<String>) {
    runApplication<HalooglasiTelegramBotApplication>(*args)
}
