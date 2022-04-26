package tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.country.rest.handler

import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dto.CountryDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository.CountryReactiveRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.handler.CountryHandler
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.initializer.Initializer
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO.Companion.toDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonsByCountry
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonReactiveRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CountryRouterTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val countryReactiveRepository: CountryReactiveRepository,
    @Autowired private val personReactiveRepository: PersonReactiveRepository,
    @Autowired private val countryRepository: CountryRepository,
    ){

    @BeforeAll
    fun beforeAll() {
        runBlocking{
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
        }
    }

    @BeforeEach
    fun beforeEach() {
        runBlocking{
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
        }    }

    @AfterEach
    fun afterEach() {
        runBlocking{
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
        }    }

    @AfterAll
    fun afterAll() {
        runBlocking{
            personReactiveRepository.deleteAll().awaitSingleOrNull()
            countryReactiveRepository.deleteAll().awaitSingleOrNull()
        }    }

    @Test
    fun `empty list if empty repository on get all`() {
        runBlocking {
            webTestClient
                .get()
                .uri("/country/all")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<CountryDTO>()
                .hasSize(0)
        }
    }

    @Test
    fun `list of one element if repository have one on get all`() {
        runBlocking {
            val country =
                Country.of(
                    "AF",
                    "Afghanistan",
                    "AFG",
                    4,
                    93
                )
            countryRepository
                .saveCountry(
                    country.get()
                )
            webTestClient
                .get()
                .uri("/country/all")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<CountryDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `list of one element if repository have one on get by code`() {
        runBlocking {
            val code = "AF"
            val country =
                Country.of(
                    code,
                    "Afghanistan",
                    "AFG",
                    4,
                    93
                )
            countryRepository
                .saveCountry(
                    country.get()
                )
            webTestClient
                .get()
                .uri("/country/code/$code")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<CountryDTO>()
                .hasSize(1)
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
            val x = countryRepository.saveCountry(countrySave)
            println(x.isPresent)
            personReactiveRepository.save(resultPerson.toDAO()).blockOptional().get()
            webTestClient
                .get()
                .uri("/country/personPerCountry")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonsByCountry>()
                .hasSize(1)
        }
    }

    @Test
    fun `list of one element if repository have one country and one user on FrudsterPerCountry`() {
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
            val x = countryRepository.saveCountry(countrySave)
            println(x.isPresent)
            val flag = true
            personReactiveRepository.save(resultPerson.toDAO()).blockOptional().get()
            webTestClient
                .get()
                .uri("/country/fraudPerCountry/$flag")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonsByCountry>()
                .hasSize(1)
        }
    }

    @Test
    fun `error  if bad request  on FrudsterPerCountry`() {
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
            val x = countryRepository.saveCountry(countrySave)
            println(x.isPresent)
            val flag = "truedd"
            personReactiveRepository.save(resultPerson.toDAO()).blockOptional().get()
            webTestClient
                .get()
                .uri("/country/fraudPerCountry/$flag")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonsByCountry>()
                .hasSize(0)
        }
    }
    @Test
    fun `ok  if valid true  on FrudsterPerCountry`() {
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
            val x = countryRepository.saveCountry(countrySave)
            println(x.isPresent)
            val flag = "true"
            personReactiveRepository.save(resultPerson.toDAO()).awaitSingleOrNull()
            webTestClient
                .get()
                .uri("/country/fraudPerCountry/$flag")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<PersonsByCountry>()
                .hasSize(1)
        }
    }

    @Test
    fun `bad request if repository is empty`() {
        runBlocking {
            val code = "ff"
            webTestClient
                .get()
                .uri("/code/$code")
                .exchange()
                .expectStatus()
                .isNotFound
                .expectBodyList<CountryHandler.CountryWithCodeNotFound>()
                .hasSize(1)
        }
    }

}