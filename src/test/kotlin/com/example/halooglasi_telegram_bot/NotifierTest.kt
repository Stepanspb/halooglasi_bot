package com.example.halooglasi_telegram_bot

import com.example.halooglasi_telegram_bot.BaseTest
import com.example.halooglasi_telegram_bot.dao.Apartment
import com.example.halooglasi_telegram_bot.notifier.Notifier
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.web.client.RestTemplate


class NotifierTest : BaseTest() {

    @Autowired
    lateinit var notifier : Notifier

    @MockBean
    lateinit var restTemplate : RestTemplate

    @Test
    internal fun name() {
        val message = "https://api.telegram.org/bot5576721520:AAGln1-ifrueVgdCdKI8XTqcOThxEPRxsQ0/sendMessage?chat_id=@appartments_halo&text=title%0A400 euro.%0Ahttps://www.halooglasi.com/relativeUrl"

        val apartment = Apartment(uid = 123, relativeUrl = "/relativeUrl",
        title = "title", price = "400")
        notifier.notify(apartment)

        Mockito.verify(restTemplate).getForEntity(message, String::class.java)
    }
}