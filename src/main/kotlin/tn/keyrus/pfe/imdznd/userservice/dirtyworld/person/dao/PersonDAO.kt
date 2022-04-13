package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

@Table("person")
data class PersonDAO(
    @Id
    val id: Long? = null,
    val seqUser: Int = 0,
    val failedSignInAttempts: Int = 0,
    val birthYear: Year = Year.of(1990),
    val state: Person.PersonState = Person.PersonState.INACTIVE,
    val country: Country = Country.of("x", "x", "x", 0, 0).get(),
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val termsVersion: LocalDate = LocalDate.now(),
    val phoneCountry: String = "",
    val kyc: Person.PersonKYC = Person.PersonKYC.NONE,
    val hasEmail: Boolean = false,
    val numberOfFlags: Int = 0,
    val fraudster: Boolean = false,
) {
    fun toPerson() =
        Person.of(
            seqUser,
            failedSignInAttempts,
            birthYear,
            country,
            createdDate,
            termsVersion,
            phoneCountry,
            kyc,
            state,
            hasEmail,
            numberOfFlags,
            fraudster,
        )

    companion object {
        fun Person.toDAO() =
            PersonDAO(
                seqUser = seqUser,
                failedSignInAttempts = failedSignInAttempts,
                birthYear = birthYear,
                country = country,
                createdDate = createdDate,
                termsVersion = termsVersion,
                phoneCountry = phoneCountry,
                kyc = kyc,
                state = state,
                hasEmail = hasEmail,
                numberOfFlags = numberOfFlags,
                fraudster = fraudster,
            )
    }
}