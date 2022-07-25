package com.example.halooglasi_telegram_bot.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ApartmentDto(
    @JsonProperty("RelativeUrl")
    val relativeUrl: String? = null,
    @JsonProperty("Id")
    val id: Long? = null,
    @JsonProperty("Title")
    val title: String? = null,
    @JsonProperty("ListHTML")
    val listHTML: String? = null
) {

    fun extractPrice(): String? {
        return listHTML?.substringAfter("span data-value=&quot;")
            ?.substringBefore("&quot")
    }
}