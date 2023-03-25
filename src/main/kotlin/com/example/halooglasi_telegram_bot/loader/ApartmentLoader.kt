package com.example.halooglasi_telegram_bot.loader

import com.example.halooglasi_telegram_bot.dao.Apartment
import com.example.halooglasi_telegram_bot.dao.ApartmentRepository
import com.example.halooglasi_telegram_bot.model.ApartmentResponse
import com.example.halooglasi_telegram_bot.model.TooManyRequestsError
import com.example.halooglasi_telegram_bot.notifier.Notifier
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException.TooManyRequests

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
                Thread.sleep(5000)
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
                    sendMessageToTelegram(newApartment)
                }
            }
        }
    }

    private fun sendMessageToTelegram(apartment: Apartment) {
        try {
            notifier.notify(apartment)
            Thread.sleep(5000)
        } catch (ex: TooManyRequests) {
            kotlin.runCatching {
                logger.error("error was occurred during sending message to telegram")
                val tooManyRequestsError = objectMapper.readValue(ex.responseBodyAsString, TooManyRequestsError::class.java)
                logger.error("error {}", tooManyRequestsError)

                var retryAfter = (tooManyRequestsError.parameters?.get("retry_after") as Int)
                if (retryAfter == 0) {
                    Thread.sleep(60000)
                } else {
                    Thread.sleep(retryAfter.toLong() + 1000)
                }
                notifier.notify(apartment)
            }.onFailure {
                logger.error(ex.message, ex)
                Thread.sleep(60000)
                notifier.notify(apartment)
            }
        }
    }
}