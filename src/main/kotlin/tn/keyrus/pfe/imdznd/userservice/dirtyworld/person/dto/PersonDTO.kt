package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto

import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.date.DateDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.date.DateDTO.Companion.toDateDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.localdate.LocalDateDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.localdate.LocalDateDTO.Companion.toLocalDateDTO

data class PersonDTO(
    val personId: Long? = null,
    val seqUser: Int,
    val failedSignInAttempts: Int,
    val birthYear: Int,
    val countryCode: String,
    val createdDate: LocalDateDTO,
    val termsVersion: DateDTO,
    val phoneCountry: String,
    val kyc: Person.PersonKYC,
    val state: Person.PersonState,
    val hasEmail: Boolean,
    val numberOfFlags: Int,
    val fraudster: Boolean,
) {

    companion object {
        fun Person.toPersonDTO() =
            PersonDTO(
                this.personId,
                this.seqUser,
                this.failedSignInAttempts,
                this.birthYear,
                this.countryCode,
                this.createdDate.toLocalDateDTO(),
                this.termsVersion.toDateDTO(),
                this.phoneCountry,
                this.kyc,
                this.state,
                this.hasEmail,
                this.numberOfFlags,
                this.fraudster
            )
    }

    fun toPerson() =
        Person.of(
            this.personId,
            this.seqUser,
            this.failedSignInAttempts,
            this.birthYear,
            this.countryCode,
            this.createdDate.toLocalDateTime().get(),
            this.termsVersion.toLocalDate().get(),
            this.phoneCountry,
            this.kyc,
            this.state,
            this.hasEmail,
            this.numberOfFlags,
            this.fraudster
        )

}