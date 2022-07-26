package com.example.halooglasi_telegram_bot.controller

import com.example.halooglasi_telegram_bot.loader.ApartmentLoader
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("init")
class InitialLoadController(val apartmentLoader: ApartmentLoader) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping()
    fun init() : ResponseEntity<Unit> {
        logger.warn("base initialization have been started")
        apartmentLoader.loadWithoutNotify()
        logger.warn("base initialization have been finished")

        return ResponseEntity.ok().build()
    }
}