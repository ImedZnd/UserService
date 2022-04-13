package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country


@Table("country")
data class CountryDAO(
    @Id
    val code: String = "A",
    val name: String = "A",
    val code3: String = "A",
    val numCode: Int = 1,
    val phoneCode: Int = 1,
) {

    fun toCountry() =
        Country.of(
            this.code,
            this.name,
            this.code3,
            this.numCode,
            this.phoneCode
        )

    companion object {
        fun Country.toDAO() =
            CountryDAO(
                code = code,
                name = name,
                code3 = code3,
                numCode = numCode,
                phoneCode = phoneCode,
            )
    }
}