package com.example.halooglasi_telegram_bot.loader

import com.example.halooglasi_telegram_bot.model.ApartmentDto
import com.example.halooglasi_telegram_bot.model.CityExpertApartment
import com.example.halooglasi_telegram_bot.model.CityExpertResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@Order(value = 2)
class CityExpertLoader(val restTemplate: RestTemplate) : Loader {

    val request =
        """https://cityexpert.rs/api/Search?req={"ptId":[],"cityId":1,"rentOrSale":"r","currentPage":%d,"resultsPerPage":30,"floor":[],"avFrom":false,"underConstruction":false,"furnished":[1],"furnishingArray":["furInternet"],"heatingArray":[],"parkingArray":[],"petsArray":[2],"minPrice":null,"maxPrice":null,"minSize":null,"maxSize":null,"polygonsArray":[],"searchSource":"regular","sort":"datedsc","structure":[],"propIds":[],"filed":[],"ceiling":[],"bldgOptsArray":[],"joineryArray":[],"yearOfConstruction":[],"otherArray":[]}"""

    val path = "https://cx.rs/r/%d"
    override fun load(page: Int): List<ApartmentDto> {
        val apartments = restTemplate.getForEntity(String.format(request, page), CityExpertResponse::class.java)
        if (apartments.body == null) {
            return listOf()
        }
        return convertToDto(apartments.body!!.result)
    }

    private fun convertToDto(result: List<CityExpertApartment>) :  List<ApartmentDto> {
       return result.map { apartment ->
            ApartmentDto(String.format(path, apartment.propId), apartment.propId,
                "${apartment.street} ${apartment.size}m", null)
        }
    }
}