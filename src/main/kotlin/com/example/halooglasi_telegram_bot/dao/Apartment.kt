package com.example.halooglasi_telegram_bot.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("apartments")
class Apartment(
    @Id
    var id : String? = null,
    @Indexed
    val uid: Long? = null,
    val relativeUrl: String? = null,
    val title: String? = null,
    var price: String? = null,
    var footage: String? = null,
    var location: String? = null,
    val priceHistory: MutableList<PriceHistory> = mutableListOf()
) {

    override fun toString(): String {
        return "Apartment(id=$id, uid=$uid, relativeUrl=$relativeUrl, title=$title, price=$price, priceHistory=$priceHistory)"
    }
}

data class PriceHistory(
    val price: String,
    val date: LocalDateTime
)

