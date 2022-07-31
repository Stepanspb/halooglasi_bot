package com.example.halooglasi_telegram_bot.loader

import com.example.halooglasi_telegram_bot.model.ApartmentDto

interface Loader {

    fun load(page : Int) : List<ApartmentDto>
}