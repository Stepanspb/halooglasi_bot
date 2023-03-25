package com.example.halooglasi_telegram_bot.loader

import com.example.halooglasi_telegram_bot.dao.Apartment
import com.example.halooglasi_telegram_bot.dao.ApartmentRepository
import com.example.halooglasi_telegram_bot.model.ApartmentResponse
import com.example.halooglasi_telegram_bot.notifier.Notifier
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ApartmentLoader(
    private val objectMapper: ObjectMapper,
    private val apartmentRepository: ApartmentRepository,
    private val notifier: Notifier,
    @Value("\${halooglasi.url}") val requestUrl: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(initialDelayString = "\${halooglasi.initialDelay}", fixedDelayString = "\${halooglasi.delay}")
    fun loadWithNotify() {
        process(true, 20)
    }

    fun loadWithoutNotify() {
        process(false, 100)
    }


    private fun process(notifyEnabled: Boolean, pageCounts: Int){
        try {
            for (page in 1..pageCounts) {
                val connection = Jsoup.connect(String.format(requestUrl, page))
                val doc = connection.get()
                val htmlString = doc.select("div[id=ad-list-2]").select("script").get(0).html()
                val json = htmlString.substringAfter("QuidditaEnvironment.serverListData=")
                    .substringBefore(";var ")
                val apartments = objectMapper.readValue(json, ApartmentResponse::class.java)
                processApartments(apartments, notifyEnabled)
                Thread.sleep(10000)
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }

    fun processApartments(apartmentResponse: ApartmentResponse, notifyEnabled : Boolean) {
        apartmentResponse.appartments.forEach {
            val dbApartment = apartmentRepository.findByUid(it.id)
            if (dbApartment == null) {
                val newApartment = apartmentRepository.save(
                    Apartment(
                        uid = it.id,
                        relativeUrl = it.relativeUrl,
                        title = it.title,
                        price = it.extractPrice()
                    )
                )
                if (notifyEnabled) {
                    notifier.notify(newApartment)
                }
            }
        }
    }
}