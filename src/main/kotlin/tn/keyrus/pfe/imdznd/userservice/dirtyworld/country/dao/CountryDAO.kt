package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao

import org.apache.logging.log4j.util.Strings
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country

@Table("country")
data class CountryDAO(
    @Id
    val code: String = "",
    val name: String = "",
    val code3ISO: String = "",
    val numCode: Int = 1,
    val phoneCode: Int = 1,
) : Persistable<String> {

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

    override fun getId(): String = code

    override fun isNew(): Boolean = Strings.isNotEmpty(code)

}