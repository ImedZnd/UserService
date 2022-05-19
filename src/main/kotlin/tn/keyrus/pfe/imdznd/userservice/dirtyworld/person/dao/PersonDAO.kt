package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.LocalDateTime

@Table("person")
data class PersonDAO(

    @Id
    @Column("id")
    val personId: Long? = null,
    val seqUser: Int = 0,
    val failedSignInAttempts: Int = 0,
    val birthYear: Int = 1990,
    val state: Person.PersonState = Person.PersonState.INACTIVE,
    val countryCode: String = "",
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
            personId,
            seqUser,
            failedSignInAttempts,
            birthYear,
            countryCode,
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
                personId = personId,
                seqUser = seqUser,
                failedSignInAttempts = failedSignInAttempts,
                birthYear = birthYear,
                countryCode = countryCode,
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