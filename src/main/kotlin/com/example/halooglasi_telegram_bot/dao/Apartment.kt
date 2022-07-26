package com.example.halooglasi_telegram_bot.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("apartments")
class Apartment(
    @Id
    var id : String? = null,
    @Indexed
    val uid: Long? = null,
    val relativeUrl: String? = null,
    val title: String? = null,
    val price: String? = null
) {

    override fun toString(): String {
        return "Apartment(id=$id, uid=$uid, relativeUrl=$relativeUrl, title=$title, price=$price)"
    }
}

