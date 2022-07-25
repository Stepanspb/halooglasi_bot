package com.example.halooglasi_telegram_bot.notifier

import com.example.halooglasi_telegram_bot.dao.Apartment
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class TelegramNotifier(
    @Value("\${telegram_bot.token}") val botToken : String,
    @Value("\${telegram_channel.name}") val chatId : String,
    val restTemplate: RestTemplate,
) : Notifier {
    private val url = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s"
    private val halooglasiPath = "https://www.halooglasi.com"

    override fun notify(apartment: Apartment) {
        val message = createMessage(apartment)
    }

    private fun createMessage(apartment: Apartment) : String {
        val builder = StringBuilder()
        apartment.title?.let { builder.append(it).append(" ") }
        apartment.price?.let { builder.append(it).append(" euro. ") }
        apartment.relativeUrl?.let { builder.append(halooglasiPath).append(it).append(" ") }
        return builder.toString()
    }

    private fun sendMessageToTelegram(message : String) {
        val request = String.format(url, botToken, chatId, message)
        restTemplate.getForEntity(request, String::class.java)
    }
}