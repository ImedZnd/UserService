package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto

import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

class PersonDTO (
    val seqUser: Int,
    val failedSignInAttempts: Int,
    val birthYear: Year,
    val state: Person.PersonState,
    val countryCode: String,
    val createdDate: LocalDateTime,
    val termsVersion: LocalDate,
    val phoneCountry: String,
    val kyc: Person.PersonKYC,
    val hasEmail: Boolean,
    val numberOfFlags: Int,
    val fraudster: Boolean,
    ){

    companion object Builder {
        fun Person.toPersonDTO() =
            PersonDTO(
                this.seqUser,
                this.failedSignInAttempts,
                this.birthYear,
                this.state,
                this.countryCode,
                this.createdDate,
                this.termsVersion,
                this.phoneCountry,
                this.kyc,
                this.hasEmail,
                this.numberOfFlags,
                this.fraudster
            )
    }

//    fun toPerson(){
//        return Person
//    }
}