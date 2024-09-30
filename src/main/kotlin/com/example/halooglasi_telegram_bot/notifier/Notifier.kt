package com.example.halooglasi_telegram_bot.notifier

import com.example.halooglasi_telegram_bot.dao.Apartment

interface Notifier {
    fun notify(apartment : Apartment, priceWasChanged : Boolean = false)
}