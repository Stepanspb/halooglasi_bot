package com.example.halooglasi_telegram_bot

import com.example.halooglasi_telegram_bot.dao.ApartmentRepository
import com.example.halooglasi_telegram_bot.notifier.Notifier
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.mockk.impl.annotations.MockK
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = ["application-test.properties"])
@Testcontainers
class BaseTest {

    @Autowired
    protected lateinit var apartmentRepository: ApartmentRepository

    companion object {
        @Container
        var mongoDBContainer = MongoDBContainer("mongo:latest")

        @JvmStatic
        @DynamicPropertySource
        fun mongoDbProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") { mongoDBContainer.replicaSetUrl }
        }
    }

    object MockitoHelper {
        fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> uninitialized(): T = null as T
    }

    @BeforeEach
    fun beforeEach() {
        apartmentRepository.deleteAll()
    }
}