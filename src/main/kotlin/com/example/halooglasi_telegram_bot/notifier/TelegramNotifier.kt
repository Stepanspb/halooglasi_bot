package com.example.halooglasi_telegram_bot.notifier

import com.example.halooglasi_telegram_bot.dao.Apartment
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*


@Service
class TelegramNotifier(
    @Value("\${telegram_bot.token}") val botToken: String,
    @Value("\${telegram_channel.name}") val chatId: String,
    val restTemplate: RestTemplate,
) : Notifier {
    private val url = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=HTML"
    private val halooglasiPath = "https://www.halooglasi.com"

    private val logger = LoggerFactory.getLogger(javaClass)


    override fun notify(apartment: Apartment, priceWasChanged: Boolean) {
        kotlin.runCatching {
            val message = createMessage(apartment, priceWasChanged)
            sendMessageToTelegram(message)
        }.onFailure {
            logger.error("Error while sending message to telegram for apartment $apartment", it)
            sendMessageToTelegram("Error while sending message to telegram for apartment $apartment")
        }
    }

    private fun createMessage(apartment: Apartment, priceWasChanged: Boolean): String {
        logger.warn("apartment : {}", apartment)
        val builder = StringBuilder()
        if (priceWasChanged) {
            builder.append("<b>Внимание! Изменена цена на квартиру:</b> \n")
            apartment.priceHistory.sortBy { it.date }
            val priceChanging = apartment.priceHistory.takeLast(5).joinToString(prefix = "--> ", separator = "\n--> ") {
                "<i>${it.price}</i> € (${it.date.toLocalDate()})"
            }
            builder.append(priceChanging).append("\n")
        }

        apartment.title?.let { builder.append("<b>").append(it.replace("#", "").trim()).append("</b>").append("\n") }
        apartment.location?.let { builder.append(it.trim()).append("\n") }
        apartment.footage?.let { builder.append("площадь: ").append(it.trim()).append("м².").append("\n") }
        apartment.price?.let { builder.append("текущая цена: ").append("<i>").append(it.trim()).append("€.").append("</i>").append("\n") }
        apartment.relativeUrl?.let { builder.append(halooglasiPath).append(it) }
        return builder.toString()
    }

    private fun sendMessageToTelegram(message: String) {
        val request = String.format(url, botToken, chatId, message)

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        restTemplate.exchange(request, HttpMethod.GET, HttpEntity<String>(headers), String::class.java)
    }
}