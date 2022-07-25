package com.example.halooglasi_telegram_bot

import com.example.halooglasi_telegram_bot.BaseTest.MockitoHelper.anyObject
import com.example.halooglasi_telegram_bot.answers.Answer
import com.example.halooglasi_telegram_bot.answers.Answer2
import com.example.halooglasi_telegram_bot.loader.ApartmentLoader
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired

@WireMockTest(httpPort = 8080)
class HalooglasiTelegramBotApplicationTests : BaseTest() {

    @Autowired
    lateinit var objectMapper : ObjectMapper

    @Autowired
    lateinit var apartmentLoader: ApartmentLoader

    @Test
    internal fun apartmentLoaderTest() {
        stubHaloOglasiResponse(Answer.answer)
        apartmentLoader.loadWithNotify()

        assert(apartmentRepository.findAll().size == 20)

        verify(notifier, times(20)).notify(anyObject())

        apartmentLoader.loadWithNotify()
        assert(apartmentRepository.findAll().size == 20)

        verify(notifier, times(20)).notify(anyObject())
    }

    @Test
    internal fun `Apartments were once and and notification was sent once`() {
        stubHaloOglasiResponse(Answer.answer)
        apartmentLoader.loadWithNotify()

        assert(apartmentRepository.findAll().size == 20)
        verify(notifier, times(20)).notify(anyObject())


        stubHaloOglasiResponse(Answer2.answer)
        apartmentLoader.loadWithNotify()
        assert(apartmentRepository.findAll().size == 21)
        verify(notifier, times(21)).notify(anyObject())
    }


    fun stubHaloOglasiResponse(answer : String) {
        stubFor(
            get(urlPathMatching("/nekretnine.*"))
            .willReturn(
                aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/html")
                .withBody(answer)));
    }


}
