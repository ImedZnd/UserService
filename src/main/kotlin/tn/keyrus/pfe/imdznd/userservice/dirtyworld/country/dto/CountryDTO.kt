package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dto

import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country

data class CountryDTO(
    val code: String,
    val name: String,
    val code3: String,
    val numCode: Int,
    val phoneCode: Int,
) {

    companion object Builder {
        fun toCountryDTO(country: Country) =
            CountryDTO(
                country.code,
                country.name,
                country.code3,
                country.numCode,
                country.phoneCode,
            )
    }
}