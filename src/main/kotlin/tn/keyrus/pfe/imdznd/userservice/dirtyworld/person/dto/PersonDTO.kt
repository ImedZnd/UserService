package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto

import io.vavr.control.Either
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.DateDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.DateDTO.Builder.toDateDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.YearDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.YearDTO.Builder.toYearDTO
import java.time.LocalDateTime

data class PersonDTO (
    val personId: Long? = null,
    val seqUser: Int,
    val failedSignInAttempts: Int,
    val birthYear: YearDTO,
    val countryCode: String,
    val createdDate: LocalDateTime,
    val termsVersion: DateDTO,
    val phoneCountry: String,
    val kyc: Person.PersonKYC,
    val state: Person.PersonState,
    val hasEmail: Boolean,
    val numberOfFlags: Int,
    val fraudster: Boolean,
    ){

    companion object {
        fun Person.toPersonDTO() =
            PersonDTO(
                this.personId,
                this.seqUser,
                this.failedSignInAttempts,
                this.birthYear.toYearDTO(),
                this.countryCode,
                this.createdDate,
                this.termsVersion.toDateDTO(),
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
            this.birthYear.toYear().get(),
            this.countryCode,
            this.createdDate,
            this.termsVersion.toLocalDate().get(),
            this.phoneCountry,
            this.kyc,
            this.state,
            this.hasEmail,
            this.numberOfFlags,
            this.fraudster

        )
    }
}