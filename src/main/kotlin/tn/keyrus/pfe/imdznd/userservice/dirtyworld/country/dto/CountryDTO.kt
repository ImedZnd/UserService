package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dto

import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country

data class CountryDTO(
    val code: String,
    val name: String,
    val codeISO: String,
    val numCode: Int,
    val phoneCode: Int,
) {

    companion object {
        fun Country.toCountryDTO() =
            CountryDTO(
                this.code,
                this.name,
                this.codeISO,
                this.numCode,
                this.phoneCode,
            )
    }
}