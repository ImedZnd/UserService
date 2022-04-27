package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto

import io.vavr.control.Either
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

class PersonDTO (
    val personId: Long? = null,
    val seqUser: Int,
    val failedSignInAttempts: Int,
    val birthYear: Year,
    val countryCode: String,
    val createdDate: LocalDateTime,
    val termsVersion: LocalDate,
    val phoneCountry: String,
    val kyc: Person.PersonKYC,
    val state: Person.PersonState,
    val hasEmail: Boolean,
    val numberOfFlags: Int,
    val fraudster: Boolean,
    ){

    companion object Builder {
        fun Person.toPersonDTO() =
            PersonDTO(
                this.personId,
                this.seqUser,
                this.failedSignInAttempts,
                this.birthYear,
                this.countryCode,
                this.createdDate,
                this.termsVersion,
                this.phoneCountry,
                this.kyc,
                this.state,
                this.hasEmail,
                this.numberOfFlags,
                this.fraudster
            )
    }

    fun toPerson(): Either<Sequence<Person.PersonError>, Person> {
        return Person.of(
            this.personId,
            this.seqUser,
            this.failedSignInAttempts,
            this.birthYear,
            this.countryCode,
            this.createdDate,
            this.termsVersion,
            this.phoneCountry,
            this.kyc,
            this.state,
            this.hasEmail,
            this.numberOfFlags,
            this.fraudster

        )
    }
}