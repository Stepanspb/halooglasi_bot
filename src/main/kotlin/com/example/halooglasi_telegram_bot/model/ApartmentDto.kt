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

    fun extractFootage(): String? {
        return listHTML?.substringAfter("class=&#39;col-p-1-3&#39;&gt;&lt;div class=&#39;value-wrapper&#39;&gt;")?.substringBefore("&amp;nbsp;m")
    }

    fun extractLocation(): String? {
       return listHTML?.substringAfter("class=&quot;subtitle-places")
           ?.substringBefore("&amp;nbsp;&lt;/li&gt;&lt;/ul&gt;&lt;ul class=&quot;product-features")
           ?.splitToSequence("nbsp")
           ?.filter { it.isNotBlank() }
           ?.toList()?.joinToString(separator = ", ") {
               it.substringAfter("&gt;&lt;li&gt;").substringBefore("&amp;")
           }
    }
}