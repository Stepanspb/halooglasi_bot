package com.example.halooglasi_telegram_bot.controller

import com.example.halooglasi_telegram_bot.loader.ApartmentLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("init")
class InitialLoadController(val apartmentLoader: ApartmentLoader) {

    @GetMapping()
    fun init() {
        apartmentLoader.loadWithoutNotify()
    }
}