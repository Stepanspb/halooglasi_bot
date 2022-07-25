package com.example.halooglasi_telegram_bot.dao

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ApartmentRepository : MongoRepository<Apartment, String> {

    fun findByUid(uid : Long?) : Apartment?
}