package com.example.halooglasi_telegram_bot.model

import com.fasterxml.jackson.annotation.JsonProperty

data class HalooglasiApartmentResponse(
    @JsonProperty("Ads")
    val appartments: List<ApartmentDto>,
    @JsonProperty("TotalCount")
    val totalCount: Int,
    @JsonProperty("TotalPages")
    val totalPages: Int,
)