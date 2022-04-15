package tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model

import io.vavr.control.Either
import org.apache.logging.log4j.util.Strings
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.Optional

class Person private constructor(
    val seqUser: Int,
    val failedSignInAttempts: Int,
    val birthYear: Year,
    val state: PersonState,
    val countryCode: String,
    val createdDate: LocalDateTime,
    val termsVersion: LocalDate,
    val phoneCountry: String,
    val kyc: PersonKYC,
    val hasEmail: Boolean,
    val numberOfFlags: Int,
    val fraudster: Boolean,
) {

    companion object Builder {
        fun of(
            seqUser: Int,
            failedSignInAttempts: Int,
            birthYear: Year,
            countryCode: String,
            createdDate: LocalDateTime,
            termsVersion: LocalDate,
            phoneCountry: String,
            kyc: PersonKYC,
            state: PersonState,
            hasEmail: Boolean,
            numberOfFlags: Int,
            fraudster: Boolean,
        ) =
            checkPerson(
                seqUser,
                failedSignInAttempts,
                birthYear,
                countryCode,
                createdDate,
                termsVersion,
                phoneCountry,
                numberOfFlags
            )
                .let {
                    checkPersonErrors(
                        it,
                        seqUser,
                        failedSignInAttempts,
                        birthYear,
                        state,
                        countryCode,
                        createdDate,
                        termsVersion,
                        phoneCountry,
                        kyc,
                        hasEmail,
                        numberOfFlags,
                        fraudster,
                    )
                }

        private fun checkPersonErrors(
            personErrors: Sequence<PersonError>,
            seqUser: Int,
            failedSignInAttempts: Int,
            birthYear: Year,
            state: PersonState,
            countryCode: String,
            createdDate: LocalDateTime,
            termsVersion: LocalDate,
            phoneCountry: String,
            kyc: PersonKYC,
            hasEmail: Boolean,
            numberOfFlags: Int,
            fraudster: Boolean,
        ): Either<Sequence<PersonError>, Person> =
            if (personErrors.count() == 0)
                Either.right(
                    Person(
                        seqUser,
                        failedSignInAttempts,
                        birthYear,
                        state,
                        countryCode,
                        createdDate,
                        termsVersion,
                        phoneCountry,
                        kyc,
                        hasEmail,
                        numberOfFlags,
                        fraudster,
                    )
                )
            else
                Either.left(personErrors)

        private fun checkPerson(
            seqUser: Int,
            failedSignInAttempts: Int,
            birthYear: Year,
            countryCode: String,
            createdDate: LocalDateTime,
            termsVersion: LocalDate,
            phoneCountry: String,
            numberOfFlags: Int,
        ): Sequence<PersonError> =
            sequenceOf(
                checkSeqUser(seqUser),
                checkFailedSignInAttempts(failedSignInAttempts),
                checkBirthYear(birthYear),
                checkCountry(countryCode),
                checkCreatedDate(createdDate),
                checkTermsVersion(termsVersion),
                checkPhoneCountry(phoneCountry),
                checkNumberOfFlags(numberOfFlags)
            )
                .filter { it.isPresent }
                .map { it.get() }

        private fun checkNumberOfFlags(numberOfFlags: Int) =
            checkIntegerFields(numberOfFlags, PersonError.PersonNumberOfFlagsError)

        private fun checkPhoneCountry(phoneCountry: String) =
            checkField(
                phoneCountry,
                PersonError.PersonPhoneCountryError,
                String::isNotEmpty
            )

        private fun checkTermsVersion(termsVersion: LocalDate) =
            checkField(
                termsVersion,
                PersonError.PersonTermsVersionError,
            ) { termsVersion.isAfter(LocalDate.now()).not() }

        private fun checkCreatedDate(createdDate: LocalDateTime) =
            checkField(
                createdDate,
                PersonError.PersonCreatedDateError,
            ) { createdDate.isAfter(LocalDateTime.now()).not() }

        private fun checkCountry(country: String) =
            checkField(
                country,
                PersonError.PersonCountryError,
                String::isNotEmpty
            )

        private fun checkBirthYear(birthYear: Year) =
            checkField(
                birthYear,
                PersonError.PersonBirthYearError,
            ) { birthYear.isAfter(Year.now()).not() }

        private fun checkSeqUser(seqUser: Int) =
            checkIntegerFields(seqUser, PersonError.PersonSeqUserError)

        private fun checkFailedSignInAttempts(failedSignInAttempts: Int) =
            checkIntegerFields(failedSignInAttempts, PersonError.PersonFailedSignInAttemptsError)

        private fun checkIntegerFields(int: Int, personError: PersonError) =
            checkField(
                int,
                personError,
            ) { int >= 0 }

        private fun <T> checkField(
            field: T,
            error: PersonError,
            validCondition: (T) -> Boolean
        ) =
            if (validCondition(field))
                Optional.empty()
            else
                Optional.of(error)

    }

    enum class PersonState {
        ACTIVE,
        INACTIVE
    }

    enum class PersonKYC {
        PASSED,
        FAILED,
        NONE,
    }

    sealed interface PersonError {
        object PersonSeqUserError : PersonError
        object PersonFailedSignInAttemptsError : PersonError
        object PersonBirthYearError : PersonError
        object PersonCountryError : PersonError
        object PersonCreatedDateError : PersonError
        object PersonTermsVersionError : PersonError
        object PersonPhoneCountryError : PersonError
        object PersonNumberOfFlagsError : PersonError
    }
}