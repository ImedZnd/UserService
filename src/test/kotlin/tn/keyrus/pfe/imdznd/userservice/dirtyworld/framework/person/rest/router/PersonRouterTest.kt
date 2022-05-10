package tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.person.rest.router

import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.web.reactive.function.BodyInserters
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository.CountryReactiveRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.initializer.Initializer
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.*
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO.Companion.toDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO.Companion.toPersonDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonReactiveRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PersonRouterTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val countryReactiveRepository: CountryReactiveRepository,
    @Autowired private val personReactiveRepository: PersonReactiveRepository,
    @Autowired private val countryRepository: CountryRepository,
    @Autowired private val messageSource: MessageSource,
    @Autowired private val personService: PersonService,
    ) {

    @BeforeAll
    fun beforeAll() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
        }
    }

    @BeforeEach
    fun beforeEach() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
        }
    }

    @AfterEach
    fun afterEach() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
        }
    }

    @AfterAll
    fun afterAll() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
        }
    }

    @Test
    fun `empty list if empty repository on get all`() {
        runBlocking {
            webTestClient
                .get()
                .uri("/person/all")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(0)
        }
    }

    @Test
    fun `list of one element if repository have one country and one user on personPerCountry`() {
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            val f = YearDTO(1975)
            webTestClient
                .post()
                .uri("/person/birthYear")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `list of 2 element if repository have one country and one user on personPerCountry`() {
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearDTO(1975)
            webTestClient
                .post()
                .uri("/person/birthYear")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `error of conversion to Year if year is negative`() {
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearDTO(-25)
            webTestClient
                .post()
                .uri("/person/birthYear")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("YearConversionError", null, Locale.US))
        }
    }

    @Test
    fun `list of 2 element if repository have one country and tow  user after a provided year`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearDTO(2015)
            webTestClient
                .post()
                .uri("/person/birthYearAfter")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `error if year is negative get all by birthe after year is negative`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearDTO(-20)
            webTestClient
                .post()
                .uri("/person/birthYearAfter")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("YearConversionError", null, Locale.US))
        }
    }

    @Test
    fun `error if year is negative get all by birthe before year is negative`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearDTO(-20)
            webTestClient
                .post()
                .uri("/person/birthYearBefore")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("YearConversionError", null, Locale.US))
        }
    }

    @Test
    fun `list of 2 element if repository have one country and tow  user before a provided year`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearDTO(2021)
            webTestClient
                .post()
                .uri("/person/birthYearBefore")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `list of 2 element if repository have one country and tow  user before a provided year range`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearRangeDTO(YearDTO(2010), YearDTO(2021))
            webTestClient
                .post()
                .uri("/person/birthYearBetween")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `End Year Before Start Year Error repository have one country and tow  user before a provided year range`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearRangeDTO(YearDTO(2022), YearDTO(2015))
            webTestClient
                .post()
                .uri("/person/birthYearBetween")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("EndDateBeforeStartDateError", null, Locale.US))
        }
    }

    @Test
    fun `Year not valid Error repository have one country and tow  user before a provided year range`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val f = YearRangeDTO(YearDTO(-2022), YearDTO(2015))
            webTestClient
                .post()
                .uri("/person/birthYearBetween")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(f))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("YearIsNotValidError", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if the same state is provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/state/$state")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `state error it the provided state is not valid`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val falseSate = "falseState"
            webTestClient
                .get()
                .uri("/person/state/$falseSate")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("statePersonProblem", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if the same country is provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/country/$code")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `country error it the provided country is not valid`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val falseSate = "flaseState"
            webTestClient
                .get()
                .uri("/person/country/$falseSate")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("CountryCodeError", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy are in the provided range`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateRangeDTO = DateRangeDTO(DateDTO(2015, 10, 10), DateDTO(2021, 10, 10))
            webTestClient
                .post()
                .uri("/person/createdDateRange")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateRangeDTO))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `Date range error it the provided if date range is not valid`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateRangeDTO = DateRangeDTO(DateDTO(2020, 10, 10), DateDTO(215, 10, 10))
            webTestClient
                .post()
                .uri("/person/createdDateRange")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateRangeDTO))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("DateRangeError", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy have term version in the provided range`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateRangeDTO = DateRangeDTO(DateDTO(2015, 10, 10), DateDTO(2021, 10, 10))
            webTestClient
                .post()
                .uri("/person/termVersionInRange")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateRangeDTO))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `Date range error of term version if the term version the provided is not valid`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateRangeDTO = DateRangeDTO(DateDTO(2021, 10, 10), DateDTO(2020, 10, 10))
            webTestClient
                .post()
                .uri("/person/termVersionInRange")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateRangeDTO))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("EndDateBeforeStartDateError", null, Locale.US))
        }
    }
    @Test
    fun `Date not valid error of term version if the term version the provided is not valid`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateRangeDTO = DateRangeDTO(DateDTO(2010, 15, 15), DateDTO(2020, 10, 10))
            webTestClient
                .post()
                .uri("/person/termVersionInRange")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateRangeDTO))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("DateIsNotValidError", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy have term version before the provided range`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2015, 10, 10)
            webTestClient
                .post()
                .uri("/person/termVersionBefore")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `tow persons if thy have term version after the provided range`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2009, 10, 10)
            webTestClient
                .post()
                .uri("/person/termVersionAfter")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `tow persons if thy have KYC as the one provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val KYC = Person.PersonKYC.PASSED
            webTestClient
                .get()
                .uri("/person/KYC/$KYC")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `error if thy have KYC not match any kyc`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val KYC = "kkk"
            webTestClient
                .get()
                .uri("/person/KYC/$KYC")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("KYCProblem", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy have the exact number of flags as the one provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlags/6")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `error if the number of flags in negative`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlags/-3")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("errorInNumberOfFlags", null, Locale.US))
        }
    }

    @Test
    fun `error if the number of flags not a number`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlags/hhh")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("errorInNumberOfFlags", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy have the number of flags Greater Than the one provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 7
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlagsGreaterThan/6")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `error if the number of flags greater  in negative`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlagsGreaterThan/-3")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("errorInNumberOfFlags", null, Locale.US))
        }
    }

    @Test
    fun `error if the number of flags greater  in not a number`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlagsGreaterThan/ccc")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("errorInNumberOfFlags", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy have the number of flags less Than the one provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 5
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlagsLessThan/6")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `error if the number of flags less  in negative`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlagsLessThan/-3")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("errorInNumberOfFlags", null, Locale.US))
        }
    }

    @Test
    fun `error if the number of flags less  in not a number`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/numberOfFlagsLessThan/vv")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("errorInNumberOfFlags", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy have the an active email`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 5
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/hasEmail/true")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `no if thy have the an not active email`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 5
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/hasEmail/false")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(0)
        }
    }

    @Test
    fun `tow persons if they are fraudsters`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 5
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/isFraud/true")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `no if they are not fraudsters`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 5
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/isFraud/hhh")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(0)
        }
    }

    @Test
    fun `tow persons if they phone Country provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 5
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/phoneCountry/$phoneCountry")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `no person if phone Country code not exist`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 5
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/phoneCountry/hhh")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("PhoneCountryProblem", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy have creation date same as the provided date before`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2021, 1, 1)
            webTestClient
                .post()
                .uri("/person/createdDateBefore")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `error date not valid on find all by created date before`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2021, 20, 1)
            webTestClient
                .post()
                .uri("/person/createdDateBefore")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("DateError", null, Locale.US))
        }
    }

    @Test
    fun `tow persons if thy have creation date same as the provided date after`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2015, 1, 1)
            webTestClient
                .post()
                .uri("/person/createdDateAfter")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `error date not valid on find all by created date after`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2021, 20, 1)
            webTestClient
                .post()
                .uri("/person/createdDateAfter")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("DateError", null, Locale.US))
        }
    }

    @Test
    fun `error date not valid on find all by term version  after`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2021, 20, 1)
            webTestClient
                .post()
                .uri("/person/termVersionBefore")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("DateError", null, Locale.US))
        }
    }

    @Test
    fun `error date not valid on find all by term version  before`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2021, 20, 1)
            webTestClient
                .post()
                .uri("/person/termVersionAfter")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("DateError", null, Locale.US))
        }
    }

    @Test
    fun `one element if they are fraudsters and in the country provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val inputs = FraudsterAndCountryCodeDTO(true, code)
            webTestClient
                .post()
                .uri("/person/fraudAndCountryCode")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(inputs))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(0)
        }
    }

    @Test
    fun `no element if they are no fraudsters and in the country provided`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val inputs = FraudsterAndCountryCodeDTO(false, code)
            webTestClient
                .post()
                .uri("/person/fraudAndCountryCode")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(inputs))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `no element if they are no fraudsters and in the country provided not exist`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val inputs = FraudsterAndCountryCodeDTO(false, "code")
            webTestClient
                .post()
                .uri("/person/fraudAndCountryCode")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(inputs))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("CountryCodeError", null, Locale.US))
        }
    }

    @Test
    fun `two in count all elements`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/countAllPerson")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<Int>()
                .hasSize(1)
        }
    }

    @Test
    fun `two in count  all elements ByState`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/countAllPersonByState/$state")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<Int>()
                .hasSize(1)
        }
    }

    @Test
    fun `error on count  all elements ByState if state is not valid`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/countAllPersonByState/ook")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("statePersonProblem", null, Locale.US))
        }
    }

    @Test
    fun `count two if two peron have the same provided term version `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2015,
                10,
                10,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2015, 10, 10)
            webTestClient
                .post()
                .uri("/person/countAllUsersByTermsVersion")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<Int>()
                .hasSize(1)
        }
    }

    @Test
    fun `error if the provided term version in the future`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2015,
                10,
                10,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            val dateDTO = DateDTO(2025, 20, 10)
            webTestClient
                .post()
                .uri("/person/countAllUsersByTermsVersion")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateDTO))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("DateError", null, Locale.US))
        }
    }

    @Test
    fun `count two if two peron have they are fraudsters `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2015,
                10,
                10,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/countAllUsersByIsFraud/false")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<Int>()
                .hasSize(1)
        }
    }

    @Test
    fun `count two if two peron have they are have the same country `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2015,
                10,
                10,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/countAllUsersByCountry/$code")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<Int>()
                .hasSize(1)
        }
    }

    @Test
    fun `error of country if country not valid `() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2015,
                10,
                10,
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
            val resultPerson2 = Person.of(
                null,
                521,
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
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            personReactiveRepository.save(resultPerson2.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/person/countAllUsersByCountry/count")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("CountryCodeError", null, Locale.US))
        }
    }


    @Test
    fun `one person on save valid person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            webTestClient
                .post()
                .uri("/person/save")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(resultPerson.toPersonDTO()))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(1)
        }
    }
    @Test
    fun `one person on update valid person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val per = personService.savePerson(resultPerson)
            val savedId = per.get().personId
            val secondPerson = Person.of(
                savedId,
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
                10,
                fraudster,
            ).get()
            webTestClient
                .post()
                .uri("/person/update")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(secondPerson.toPersonDTO()))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `one person on delete valid person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val per = personService.savePerson(resultPerson)
            val savedId = per.get().personId
            val personId = PersonIdDTO(savedId!!)
            webTestClient
                .post()
                .uri("/person/delete")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personId))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(1)
        }
    }
    @Test
    fun `error person not exist on delete person`() {
        runBlocking {
            val personId = PersonIdDTO(55)
            webTestClient
                .post()
                .uri("/person/delete")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personId))
                .exchange()
                .expectStatus()
                .isNotFound
                .expectHeader()
                .valueMatches("notFound", messageSource.getMessage("PersonNotExist", null, Locale.US))
        }
    }
    @Test
    fun `one person on flag a valid person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val per = personService.savePerson(resultPerson)
            val savedId = per.get().personId
            val personId = PersonIdDTO(savedId!!)
            webTestClient
                .post()
                .uri("/person/flag")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personId))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(1)

        }
    }
    @Test
    fun `one person on fraud a valid person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val per = personService.savePerson(resultPerson)
            val savedId = per.get().personId
            val personId = PersonIdDTO(savedId!!)
            webTestClient
                .post()
                .uri("/person/fraud")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personId))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(1)

        }
    }    @Test
    fun `error on fraud a valid person already fraud`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            val per = personService.savePerson(resultPerson)
            val savedId = per.get().personId
            val personId = PersonIdDTO(savedId!!)
            webTestClient
                .post()
                .uri("/person/fraud")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personId))
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectHeader()
                .valueMatches("error", messageSource.getMessage("PersonFraudsterServiceError", null, Locale.US))
        }
    }
    @Test
    fun `one person on unfraud a valid person`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val countrySave =
                Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            val per = personService.savePerson(resultPerson)
            val savedId = per.get().personId
            val personId = PersonIdDTO(savedId!!)
            webTestClient
                .post()
                .uri("/person/unfraud")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personId))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(1)

        }
    }

    @Test
    fun `error person not found on flag person not exist`() {
        runBlocking {
            val id :Long = 10
            val personId = PersonIdDTO(id)
            webTestClient
                .post()
                .uri("/person/flag")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personId))
                .exchange()
                .expectHeader()
                .valueMatches("notFound", messageSource.getMessage("PersonNotExist", null, Locale.US))
        }
    }

    @Test
    fun `error person not found on update person not exist`() {
        runBlocking {
            val code = "PY"
            val seqUser = 2993
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
                2010,
                10,
                20,
            )
            val phoneCountry = "GB||JE||IM||GG"
            val kyc = Person.PersonKYC.PASSED
            val hasEmail = true
            val numberOfFlags = 6
            val fraudster = false
            val resultPerson = Person.of(
                10,
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
            webTestClient
                .post()
                .uri("/person/update")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(resultPerson.toPersonDTO()))
                .exchange()
                .expectHeader()
                .valueMatches("notFound", messageSource.getMessage("PersonNotExist", null, Locale.US))
        }
    }


    @Test
    fun `get one valid peron on get person by id`() {
        runBlocking {
            val code = "PY"
            val name = "Paraguay"
            val code3 = "pRY"
            val numCode = 600
            val phoneCode = 595
            val seqUser = 2993
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
                2010,
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
            val per = personService.savePerson(resultPerson)
            val savedId = per.get().personId
            val personId = PersonIdDTO(savedId!!)
            webTestClient
                .post()
                .uri("/person/id")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personId))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonDTO>()
                .hasSize(1)
        }
    }

}