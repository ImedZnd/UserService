package tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.person.repository

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertAll
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository.CountryReactiveRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.initializer.Initializer
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonDatabaseRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonReactiveRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PersonDatabaseRepositoryTest(
    @Autowired private val countryReactiveRepository: CountryReactiveRepository,
    @Autowired private val countryRepository: CountryRepository,
    @Autowired private val personReactiveRepository: PersonReactiveRepository,
    @Autowired private val personDatabaseRepository: PersonDatabaseRepository,
    @Autowired private val rabbitAdmin: RabbitAdmin,
) {
    @BeforeAll
    fun beforeAll() {
        personReactiveRepository.deleteAll().subscribe()
        countryReactiveRepository.deleteAll().subscribe()
        rabbitAdmin.purgeQueue("flagpersonqueue")
        rabbitAdmin.purgeQueue("savepersonqueue")
        rabbitAdmin.purgeQueue("updatepersonqueue")
        rabbitAdmin.purgeQueue("deletepersonqueue")
    }

    @BeforeEach
    fun beforeEach() {
        personReactiveRepository.deleteAll().subscribe()
        countryReactiveRepository.deleteAll().subscribe()
        rabbitAdmin.purgeQueue("flagpersonqueue")
        rabbitAdmin.purgeQueue("savepersonqueue")
        rabbitAdmin.purgeQueue("updatepersonqueue")
        rabbitAdmin.purgeQueue("deletepersonqueue")
    }

    @AfterEach
    fun afterEach() {
        personReactiveRepository.deleteAll().subscribe()
        countryReactiveRepository.deleteAll().subscribe()
        rabbitAdmin.purgeQueue("flagpersonqueue")
        rabbitAdmin.purgeQueue("savepersonqueue")
        rabbitAdmin.purgeQueue("updatepersonqueue")
        rabbitAdmin.purgeQueue("deletepersonqueue")
    }

    @AfterAll
    fun afterAll() {
        personReactiveRepository.deleteAll().subscribe()
        countryReactiveRepository.deleteAll().subscribe()
        rabbitAdmin.purgeQueue("flagpersonqueue")
        rabbitAdmin.purgeQueue("savepersonqueue")
        rabbitAdmin.purgeQueue("updatepersonqueue")
        rabbitAdmin.purgeQueue("deletepersonqueue")
    }


    @Test
    fun `get all person return one element if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository.findAllPerson()
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one element on get person by birth year if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository.findAllPersonByBirthYear(birthYear)
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one element on get person by country if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository.findAllPersonByCountry(code)
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one element on get person by created date in range if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository.findAllPersonByCreatedDateInRange(
                    LocalDate.of(2010, 10, 10),
                    LocalDate.of(2021, 10, 10),
                )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one element on get person by terms version if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository.findAllPersonByTermsVersion(termsVersion)
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one element on get person by terms version between if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByTermsVersionBetween(
                        termsVersion,
                        LocalDate.now()
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one element on get person by phone country if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByPhoneCountry(
                        phoneCountry
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one element on get person by kyc if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByKYC(
                        kyc
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }


    @Test
    fun `one element on get person by hasEmail if repository have one valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByHasEmail(
                        hasEmail
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `on element on get person by hasEmail if repository have one valid person with no email`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByHasEmail(
                        !hasEmail
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 0)
        }
    }

    @Test
    fun `one person on get person by NumberOfFlags if repository have one valid person with no email`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByNumberOfFlags(
                        numberOfFlags
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one person on get person by NumberOfFlagsGreater if repository have one valid person with no email`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByNumberOfFlagsGreaterThan(
                        2
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one person on get person by NumberOfFlagsLesser if repository have one valid person with no email`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByNumberOfFlagsLessThan(
                        2
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 0)
        }
    }

    @Test
    fun `non on get person by FraudsterAndCountryCode if repository have one valid person `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByFraudsterAndCountryCode(
                        true, code
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 0)
        }
    }

    @Test
    fun `one on get person by FraudsterAndCountryCode if repository have one valid person `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            personDatabaseRepository.savePerson(resultPerson)
            val result =
                personDatabaseRepository
                    .findAllPersonByFraudsterAndCountryCode(
                        false,
                        code
                    )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `one on count person by BirthYear if repository have one valid person `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            personDatabaseRepository.savePerson(resultPerson)
            val result = personDatabaseRepository
                .countByBirthYear(
                    birthYear
                )
            val resNumber = 1
            assert(result == resNumber.toLong())
        }
    }

    @Test
    fun `one on count person by ByState if repository have one valid person `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            personDatabaseRepository.savePerson(resultPerson)
            val result = personDatabaseRepository
                .countAllPersonByState(
                    state
                )
            val resNumber = 1
            assert(result == resNumber.toLong())
        }
    }

    @Test
    fun `one on count person by TermsVersion if repository have one valid person `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            personDatabaseRepository.savePerson(resultPerson)
            val result = personDatabaseRepository
                .countAllPersonByTermsVersion(
                    termsVersion
                )
            val resNumber = 1
            assert(result == resNumber.toLong())
        }
    }


    @Test
    fun `one on count person by Fraudster if repository have one valid person `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            personDatabaseRepository.savePerson(resultPerson)
            val result = personDatabaseRepository
                .countAllPersonByFraudster(
                    fraudster
                )
            val resNumber = 1
            assert(result == resNumber.toLong())
        }
    }

    @Test
    fun `one on count person by Country if repository have one valid person `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            personDatabaseRepository.savePerson(resultPerson)
            val result = personDatabaseRepository
                .countAllPersonByCountry(
                    code
                )
            val resNumber = 1
            assert(result == resNumber.toLong())
        }
    }

    @Test
    fun `one on count all  person if repository have one valid person `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            personDatabaseRepository.savePerson(resultPerson)
            val result = personDatabaseRepository
                .countAllPerson()
            val resNumber = 1
            assert(result == resNumber.toLong())
        }
    }


    @Test
    fun `two element on get person by fraudster if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByIsFraudster(fraudster)
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by birth year and email if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByBirthYearAndHasEmail(birthYear, hasEmail)
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by birth yearbefore if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByBirthYearBefore(birthYear.plusYears(1))
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by birth yearAfter if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByBirthYearAfter(birthYear.minusYears(1))
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by birth yearBetween if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByBirthYearBetween(
                    birthYear.minusYears(1),
                    birthYear.plusYears(1)
                )
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by CreatedDate after if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByCreatedDateAfter(createdDate.minusYears(1))
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by CreatedDate before if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByCreatedDateBefore(createdDate.plusYears(1))
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by TermsVersion before if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByTermsVersionBefore(termsVersion.plusYears(1))
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by TermsVersion after if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByTermsVersionAfter(termsVersion.minusYears(1))
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by BirthYearAndCountryCode if repository have two valid fraudster with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
            val failedSignInAttempts = 2
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
            val numberOfFlags = 3
            val fraudster = true
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByBirthYearAndCountryCode(birthYear, code)
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by state if repository have two valid person with one country`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository
                .saveCountry(
                    countrySave
                )
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            personDatabaseRepository.savePerson(resultPerson2)
            val result =
                personDatabaseRepository.findAllPersonByState(state)
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `get all person return error if repository have one valid person with  not exist`() {
        runBlocking {
            val code = "PY"
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()
            val result =
                personDatabaseRepository.savePerson(resultPerson)
                    .left
            assertAll(
                { assert(result is PersonRepository.PersonRepositoryIOError) },
            )
        }
    }

    @Test
    fun `publishSavePerson `() {
        runBlocking {
            val code = "PY"
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()

            personDatabaseRepository.publishSavePerson(resultPerson.personId)
            Thread.sleep(1000)
            val result = rabbitAdmin.getQueueInfo("savepersonqueue").messageCount
            println(result)
            assert(result == 1)
        }
    }

    @Test
    fun `publishUpdatePerson `() {
        runBlocking {
            val code = "PY"
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()

            personDatabaseRepository.publishUpdatePerson(resultPerson.personId)
            Thread.sleep(1000)
            val result = rabbitAdmin.getQueueInfo("updatepersonqueue").messageCount
            println(result)
            assert(result == 1)
        }
    }
    @Test
    fun `publishDeletePerson `() {
        runBlocking {
            val code = "PY"
            val seqUser = 2993
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
            val resultPerson = Person.of(
                UUID.randomUUID(),
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                hasEmail,
                numberOfFlags,
                fraudster,
            ).get()

            personDatabaseRepository.publishDeletePerson(resultPerson.personId)
            Thread.sleep(1000)
            val result = rabbitAdmin.getQueueInfo("deletepersonqueue").messageCount
            println(result)
            assert(result == 1)
        }
    }
    @Test
    fun `publishFlagPerson `() {
        runBlocking {
            personDatabaseRepository.publishFlagPerson(UUID.randomUUID())
            Thread.sleep(1000)
            val result = rabbitAdmin.getQueueInfo("flagpersonqueue").messageCount
            println(result)
            assert(result == 1)
        }
    }

}