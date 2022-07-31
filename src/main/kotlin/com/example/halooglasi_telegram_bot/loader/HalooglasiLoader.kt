package com.example.halooglasi_telegram_bot.loader

import com.example.halooglasi_telegram_bot.model.ApartmentDto
import com.example.halooglasi_telegram_bot.model.HalooglasiApartmentResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

@Service
@Order(value = 1)
class HalooglasiLoader(private val objectMapper: ObjectMapper,
                       @Value("\${halooglasi.url}") val requestUrl: String,) : Loader {

    override fun load(page : Int): List<ApartmentDto> {
        val connection = Jsoup.connect(String.format(requestUrl, page))
        val doc = connection.get()
        val htmlString = doc.select("div[id=ad-list-2]").select("script").get(0).html()
        val json = htmlString.substringAfter("QuidditaEnvironment.serverListData=")
            .substringBefore(";var ")
        return objectMapper.readValue(json, HalooglasiApartmentResponse::class.java).appartments
    }
}