package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao

import org.springframework.data.relational.core.mapping.Table
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country

@Table("country")
data class CountryDAO(
    val code: String = "",
    val name: String = "",
    val code3ISO: String = "",
    val numCode: Int = 1,
    val phoneCode: Int = 1,
) {

    fun toCountry() =
        Country.of(
            this.code,
            this.name,
            this.code3ISO,
            this.numCode,
            this.phoneCode
        )

    companion object {
        fun Country.toDAO() =
            CountryDAO(
                code = code,
                name = name,
                code3ISO = codeISO,
                numCode = numCode,
                phoneCode = phoneCode,
            )
    }

}