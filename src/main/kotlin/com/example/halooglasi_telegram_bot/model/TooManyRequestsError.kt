package com.example.halooglasi_telegram_bot.model

data class TooManyRequestsError(
    val ok : Boolean?,
    val description: String?,
    val parameters : Map<String, Any?>?
)