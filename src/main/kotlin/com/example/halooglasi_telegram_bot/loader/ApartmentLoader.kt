package com.example.halooglasi_telegram_bot.loader

import com.example.halooglasi_telegram_bot.dao.Apartment
import com.example.halooglasi_telegram_bot.dao.ApartmentRepository
import com.example.halooglasi_telegram_bot.model.ApartmentDto
import com.example.halooglasi_telegram_bot.notifier.Notifier
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ApartmentLoader(
    private val objectMapper: ObjectMapper,
    private val apartmentRepository: ApartmentRepository,
    private val notifier: Notifier,
    private val loaders : List<Loader>,
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
        loaders.forEach{loader ->
            try {
                for (page in 1..pageCounts) {
                    val apartments =  loader.load(page)
                    processApartments(apartments, notifyEnabled)
                }
            } catch (e: Exception) {
                logger.error(e.message, e)
            }
        }
    }

    fun processApartments(apartments : List<ApartmentDto>, notifyEnabled : Boolean) {
        apartments.forEach {
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