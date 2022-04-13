package tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

internal class PersonTest {

    @Test
    fun `valid test Person non empty field`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val seqUser = 2993
        val failedSignInAttempts = 0
        val birthYear = Year.of(1975)
        val state = Person.PersonState.ACTIVE
        val country =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        val createdDate = LocalDateTime.of(
            2020,
            10,
            20,
            5,
            5,
            5
        )
        val termsVersion = LocalDate.of(
            2020,
            10,
            20,
        )
        val phoneCountry = "GB||JE||IM||GG"
        val kyc = Person.PersonKYC.PASSED
        val hasEmail = true
        val numberOfFlags = 6
        val fraudster = false
        val result = Person.of(
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
        ).get()
        assertAll(
            { assert(result.seqUser == seqUser) },
            { assert(result.seqUser == seqUser) },
            { assert(result.failedSignInAttempts == failedSignInAttempts) },
            { assert(result.birthYear == birthYear) },
            { assert(result.country == country) },
            { assert(result.createdDate == createdDate) },
            { assert(result.termsVersion == termsVersion) },
            { assert(result.phoneCountry == phoneCountry) },
            { assert(result.kyc == kyc) },
            { assert(result.state == state) },
            { assert(result.hasEmail == hasEmail) },
            { assert(result.numberOfFlags == numberOfFlags) },
            { assert(result.fraudster == fraudster) },
        )
    }

    @Test
    fun `seqUser error test Person Code negative field`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val country =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        val seqUser = -2
        val failedSignInAttempts = 0
        val birthYear = Year.of(1975)
        val state = Person.PersonState.ACTIVE
        val createdDate = LocalDateTime.of(
            2020,
            10,
            20,
            5,
            5,
            5
        )
        val termsVersion = LocalDate.of(
            2020,
            10,
            20,
        )
        val phoneCountry = "GB||JE||IM||GG"
        val kyc = Person.PersonKYC.PASSED
        val hasEmail = true
        val numberOfFlags = 6
        val fraudster = false
        val result = Person.of(
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
        ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Person.PersonError.PersonSeqUserError }) },
        )
    }

    @Test
    fun `failedSignInAttempts error test Person Code negative field`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val country =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        val seqUser = 34
        val failedSignInAttempts = -4
        val birthYear = Year.of(1975)
        val state = Person.PersonState.ACTIVE
        val createdDate = LocalDateTime.of(
            2020,
            10,
            20,
            5,
            5,
            5
        )
        val termsVersion = LocalDate.of(
            2020,
            10,
            20,
        )
        val phoneCountry = "GB||JE||IM||GG"
        val kyc = Person.PersonKYC.PASSED
        val hasEmail = true
        val numberOfFlags = 6
        val fraudster = false
        val result = Person.of(
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
        ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Person.PersonError.PersonFailedSignInAttemptsError }) },
        )
    }

    @Test
    fun `birthYear error Person Year in in the future`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val country =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        val seqUser = 45
        val failedSignInAttempts = 0
        val birthYear = Year.of(2050)
        val state = Person.PersonState.ACTIVE
        val createdDate = LocalDateTime.of(
            2020,
            10,
            20,
            5,
            5,
            5
        )
        val termsVersion = LocalDate.of(
            2020,
            10,
            20,
        )
        val phoneCountry = "GB||JE||IM||GG"
        val kyc = Person.PersonKYC.PASSED
        val hasEmail = true
        val numberOfFlags = 6
        val fraudster = false
        val result = Person.of(
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
        ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Person.PersonError.PersonBirthYearError }) },
        )
    }

    @Test
    fun `createdDate error Person createdDate in the future`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val country =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        val seqUser = 45
        val failedSignInAttempts = 0
        val birthYear = Year.of(2020)
        val state = Person.PersonState.ACTIVE
        val createdDate = LocalDateTime.of(
            2050,
            10,
            20,
            5,
            5,
            5
        )
        val termsVersion = LocalDate.of(
            2020,
            10,
            20,
        )
        val phoneCountry = "GB||JE||IM||GG"
        val kyc = Person.PersonKYC.PASSED
        val hasEmail = true
        val numberOfFlags = 6
        val fraudster = false
        val result = Person.of(
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
        ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Person.PersonError.PersonCreatedDateError }) },
        )
    }

    @Test
    fun `termsVersion error Person termsVersion in the future`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val country =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        val seqUser = 45
        val failedSignInAttempts = 0
        val birthYear = Year.of(2020)
        val state = Person.PersonState.ACTIVE
        val createdDate = LocalDateTime.of(
            2020,
            10,
            20,
            5,
            5,
            5
        )
        val termsVersion = LocalDate.of(
            2050,
            10,
            20,
        )
        val phoneCountry = "GB||JE||IM||GG"
        val kyc = Person.PersonKYC.PASSED
        val hasEmail = true
        val numberOfFlags = 6
        val fraudster = false
        val result = Person.of(
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
        ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Person.PersonError.PersonTermsVersionError }) },
        )
    }

    @Test
    fun `phoneCountry error Person phoneCountry is empty`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val country =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        val seqUser = 45
        val failedSignInAttempts = 0
        val birthYear = Year.of(2020)
        val state = Person.PersonState.ACTIVE
        val createdDate = LocalDateTime.of(
            2020,
            10,
            20,
            5,
            5,
            5
        )
        val termsVersion = LocalDate.of(
            2020,
            10,
            20,
        )
        val phoneCountry = ""
        val kyc = Person.PersonKYC.PASSED
        val hasEmail = true
        val numberOfFlags = 6
        val fraudster = false
        val result = Person.of(
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
        ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Person.PersonError.PersonPhoneCountryError }) },
        )
    }

    @Test
    fun `numberOfFlags error Person numberOfFlags is negative`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val country =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        val seqUser = 45
        val failedSignInAttempts = 0
        val birthYear = Year.of(2020)
        val state = Person.PersonState.ACTIVE
        val createdDate = LocalDateTime.of(
            2020,
            10,
            20,
            5,
            5,
            5
        )
        val termsVersion = LocalDate.of(
            2020,
            10,
            20,
        )
        val phoneCountry = "GB||JE||IM||GG"
        val kyc = Person.PersonKYC.PASSED
        val hasEmail = true
        val numberOfFlags = -5
        val fraudster = false
        val result = Person.of(
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
        ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Person.PersonError.PersonNumberOfFlagsError }) },
        )
    }

}