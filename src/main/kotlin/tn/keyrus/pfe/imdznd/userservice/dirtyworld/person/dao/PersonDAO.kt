package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao

import org.apache.logging.log4j.util.Strings
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.UUID

@Table("person")
data class PersonDAO(
    @Id
    @Column("id")
    var personId: UUID =UUID.randomUUID() ,
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
): Persistable<UUID> {
    fun toPerson() =
        Person.of(
            personId,
            seqUser,
            failedSignInAttempts,
            Year.of(birthYear),
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
                birthYear = birthYear.value,
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

    override fun isNew(): Boolean = Strings.isNotEmpty(this.personId.toString())

    override fun getId(): UUID = personId

}