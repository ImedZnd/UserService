package tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.person.service

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.reactor.awaitSingleOrNull
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
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository.CountryReactiveRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.initializer.Initializer
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonReactiveRepository
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PersonServiceTest(
    @Autowired private val countryReactiveRepository: CountryReactiveRepository,
    @Autowired private val countryRepository: CountryRepository,
    @Autowired private val personReactiveRepository: PersonReactiveRepository,
    @Autowired private val personService: PersonService,
    @Autowired private val rabbitAdmin: RabbitAdmin,
) {
    @BeforeAll
    fun beforeAll() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
            rabbitAdmin.purgeQueue("flagpersonqueue")
            rabbitAdmin.purgeQueue("savepersonqueue")
            rabbitAdmin.purgeQueue("updatepersonqueue")
            rabbitAdmin.purgeQueue("deletepersonqueue")
        }
    }

    @BeforeEach
    fun beforeEach() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
            rabbitAdmin.purgeQueue("flagpersonqueue")
            rabbitAdmin.purgeQueue("savepersonqueue")
            rabbitAdmin.purgeQueue("updatepersonqueue")
            rabbitAdmin.purgeQueue("deletepersonqueue")
        }
    }

    @AfterEach
    fun afterEach() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
            rabbitAdmin.purgeQueue("flagpersonqueue")
            rabbitAdmin.purgeQueue("savepersonqueue")
            rabbitAdmin.purgeQueue("updatepersonqueue")
            rabbitAdmin.purgeQueue("deletepersonqueue")
        }
    }

    @AfterAll
    fun afterAll() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
            rabbitAdmin.purgeQueue("flagpersonqueue")
            rabbitAdmin.purgeQueue("savepersonqueue")
            rabbitAdmin.purgeQueue("updatepersonqueue")
            rabbitAdmin.purgeQueue("deletepersonqueue")
        }
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService.getAllPersons()
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson).get()
            val result =
                personService.getAllPersonsByBirthYear(birthYear)
            val resultQueue = rabbitAdmin.getQueueInfo("savepersonqueue").messageCount

            val y = result.count()
            assert(y == 1)
            assert(resultQueue == 1)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService.getAllPersonByCountry(code)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService.getAllPersonByCreatedDateInRange(
                    LocalDate.of(2010, 10, 10),
                    LocalDate.of(2021, 10, 10),
                )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService.getAllPersonByTermsVersion(termsVersion)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByTermsVersionBetween(
                        termsVersion,
                        LocalDate.now()
                    )
            val resultf = rabbitAdmin.getQueueInfo("savepersonqueue").messageCount
            Thread.sleep(1000)
            val y = result.count()
            assert(resultf == 1)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByPhoneCountry(
                        phoneCountry
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByKYC(
                        kyc
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByHasEmail(
                        hasEmail
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByHasEmail(
                        !hasEmail
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByNumberOfFlags(
                        numberOfFlags
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByNumberOfFlagsGreaterThan(
                        2
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByNumberOfFlagsLessThan(
                        2
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByFraudsterAndCountryCode(
                        true, code
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result =
                personService
                    .getAllPersonByFraudsterAndCountryCode(
                        false,
                        code
                    )
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result = personService
                .countByBirthYear(
                    birthYear
                )
            val resNumber = 1
            assert(result == resNumber)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result = personService
                .countAllPersonByState(
                    state
                )
            val resNumber = 1
            assert(result == resNumber)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result = personService
                .countAllPersonByTermsVersion(
                    termsVersion
                )
            val resNumber = 1
            assert(result == resNumber)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result = personService
                .countAllPersonByFraudster(
                    fraudster
                )
            val resNumber = 1
            assert(result == resNumber)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result = personService
                .countAllPersonByCountry(
                    code
                )
            val resNumber = 1
            assert(result == resNumber)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val result = personService
                .countAllPerson()
            val resNumber = 1
            assert(result == resNumber)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByIsFraudster(fraudster)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByBirthYearAndHasEmail(birthYear, hasEmail)
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `two element on get person by birth year before if repository have two valid fraudster with one country`() {
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByBirthYearBefore(birthYear + 1)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByBirthYearAfter(birthYear - 1)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByBirthYearBetween(birthYear - 1, birthYear + 1)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByCreatedDateAfter(createdDate.minusYears(1))
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByCreatedDateBefore(createdDate.plusYears(1))
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByTermsVersionBefore(termsVersion.plusYears(1))
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByTermsVersionAfter(termsVersion.minusYears(1))
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonByBirthYearAndCountryCode(birthYear, code)
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
            val birthYear = 1975
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
                null,
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
            personService.savePerson(resultPerson)
            val resultPerson2 = Person.of(
                null,
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
            personService.savePerson(resultPerson2)
            val result =
                personService.getAllPersonsByState(state)
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
            val birthYear = 1975
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
                null,
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
                personService.savePerson(resultPerson)
                    .left
            assertAll(
                { assert(result is PersonService.PersonServiceError) },
            )
        }
    }

    @Test
    fun `update person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
            val failedSignInAttempts = 0
            val birthYear = 1975
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
                null,
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
            val idOfPerson = personService.savePerson(resultPerson).get().personId
            val resultPerson2 = Person.of(
                idOfPerson,
                seqUser,
                failedSignInAttempts,
                birthYear,
                code,
                createdDate,
                termsVersion,
                phoneCountry,
                kyc,
                state,
                false,
                numberOfFlags,
                fraudster,
            ).get()
            val mm = personService.updatePerson(resultPerson2)
            val result =
                personService.getAllPersons()
            val y = result.count()
            val resultf = rabbitAdmin.getQueueInfo("updatepersonqueue").messageCount
            val resultl = rabbitAdmin.getQueueInfo("savepersonqueue").messageCount
            assert(resultf == 1)
            assert(resultl == 1)
            assert(y == 1)
            assert(!mm.get().hasEmail)
        }
    }

    @Test
    fun `delete person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
            val failedSignInAttempts = 0
            val birthYear = 1975
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
                null,
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
            val savedPerson = personService.savePerson(resultPerson).get()
            savedPerson.personId?.let { personService.deletePerson(it) }
            val result =
                personService.getAllPersons()
            val y = result.count()
            val resultf = rabbitAdmin.getQueueInfo("deletepersonqueue").messageCount
            Thread.sleep(1000)
            assert(resultf == 1)
            assert(y == 0)
        }
    }

    @Test
    fun `flag person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
            val failedSignInAttempts = 0
            val birthYear = 1975
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
            val numberOfFlagsBefore = 6
            val fraudster = false
            val resultPerson = Person.of(
                null,
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
                numberOfFlagsBefore,
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
            val savedPerson = personService.savePerson(resultPerson).get()
            savedPerson.personId?.let { personService.flagPerson(it) }
            val result =
                personService.getAllPersons()
            val y = result.count()
            val numberOfFlags = result.first().numberOfFlags
            val resultf = rabbitAdmin.getQueueInfo("flagpersonqueue").messageCount
            assert(y == 1)
            assert(numberOfFlags == 7)
            assert(resultf == 1)
        }
    }

    @Test
    fun `error on flag person`() {
        runBlocking {
            val result = personService.flagPerson(11)
            assert(result.left is PersonService.PersonServiceError.PersonServicePersonNotExistError)
        }
    }


    @Test
    fun `error on fraud person`() {
        runBlocking {
            val result = personService.flagPerson(11)
            assert(result.left is PersonService.PersonServiceError.PersonServicePersonNotExistError)
        }
    }
    @Test
    fun `error on unFraud person`() {
        runBlocking {
            val result = personService.unFraudPerson(11)
            assert(result.left is PersonService.PersonServiceError.PersonServicePersonNotExistError)
        }
    }

    @Test
    fun `fraud person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
            val failedSignInAttempts = 0
            val birthYear = 1975
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
            val numberOfFlagsBefore = 6
            val fraudster = false
            val resultPerson = Person.of(
                null,
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
                numberOfFlagsBefore,
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
            val savedPerson = personService.savePerson(resultPerson).get()
            savedPerson.personId?.let { personService.fraudPerson(it) }
            val result =
                personService.getAllPersons()
            val y = result.count()
            val fraud = result.first().fraudster
            val resultf = rabbitAdmin.getQueueInfo("fraudpersonqueue").messageCount
            assert(y == 1)
            assert(fraud)
            assert(resultf == 1)
        }
    }

    @Test
    fun `unFraud person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
            val failedSignInAttempts = 0
            val birthYear = 1975
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
            val numberOfFlagsBefore = 6
            val fraudster = true
            val resultPerson = Person.of(
                null,
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
                numberOfFlagsBefore,
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
            val savedPerson = personService.savePerson(resultPerson).get()
            Thread.sleep(1000)
            personService.unFraudPerson(savedPerson.personId!!)
            val result =
                personService.getAllPersons()
            val y = result.count()
            Thread.sleep(1000)
            val resultf = rabbitAdmin.getQueueInfo("unfraudpersonqueue").messageCount
            assert(y == 1)
            assert(resultf == 1)
        }
    }
}