package tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.country.repository

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
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
internal class CountryDatabaseRepositoryTest(
    @Autowired private val countryReactiveRepository: CountryReactiveRepository,
    @Autowired private val countryRepository: CountryRepository,
    @Autowired private val personReactiveRepository: PersonReactiveRepository,
    @Autowired private val personDatabaseRepository: PersonDatabaseRepository
) {
    @BeforeAll
    fun beforeAll() {
        personReactiveRepository.deleteAll().subscribe()
        countryReactiveRepository.deleteAll().subscribe()
    }

    @BeforeEach
    fun beforeEach() {
        personReactiveRepository.deleteAll().subscribe()
        countryReactiveRepository.deleteAll().subscribe()
    }

    @AfterEach
    fun afterEach() {
        personReactiveRepository.deleteAll().subscribe()
        countryReactiveRepository.deleteAll().subscribe()
    }

    @AfterAll
    fun afterAll() {
        personReactiveRepository.deleteAll().subscribe()
        countryReactiveRepository.deleteAll().subscribe()
    }

    @Test
    fun `find all return one element on save operation if repository is have one element`() {
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
            val result =
                countryRepository
                    .findAllCountry()
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `find by code return one element on save operation if repository is have one element with code`() {
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
            val result =
                countryRepository
                    .findCountryByCode(code)
            assert(result.isPresent)
            assert(result.get() is Country)
        }
    }

    @Test
    fun `get all countries by person return one element if repository have one person with one country`() {
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
                countryRepository
                    .findAllCountryByNumberOfPersons()
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 1)
        }
    }

    @Test
    fun `get all users group by countries return two element if repository have one user for two country`() {
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
            val resultPerson =
                Person.of(
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
            val person2 =
                Person.of(
                    UUID.randomUUID(),
                    seqUser,
                    failedSignInAttempts,
                    birthYear,
                    "TN",
                    createdDate,
                    termsVersion,
                    phoneCountry,
                    kyc,
                    state,
                    hasEmail,
                    numberOfFlags,
                    fraudster,
                ).get()
            val countrySave = Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            val country2 = Country.of(
                    "TN",
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(country2)
            personDatabaseRepository.savePerson(resultPerson)
            personDatabaseRepository.savePerson(person2)
            val result =
                countryRepository
                    .findAllCountryByNumberOfPersons()
            result.onEach { print(it) }
            val y = result.count()
            assert(y == 2)
        }
    }

    @Test
    fun `get all fraudsters group by countries return one element if repository have one fraudster for one country`() {
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
            val resultPerson =
                Person.of(
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
            val person2 =
                Person.of(
                    UUID.randomUUID(),
                    seqUser,
                    failedSignInAttempts,
                    birthYear,
                    "TN",
                    createdDate,
                    termsVersion,
                    phoneCountry,
                    kyc,
                    state,
                    hasEmail,
                    numberOfFlags,
                    fraudster,
                ).get()
            val countrySave = Country.of(
                    code,
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(countrySave)
            val country2 = Country.of(
                    "TN",
                    name,
                    code3,
                    numCode,
                    phoneCode
                ).get()
            countryRepository.saveCountry(country2)
            personDatabaseRepository.savePerson(resultPerson)
            personDatabaseRepository.savePerson(person2)
            val result =
                countryRepository
                    .findAllIsFraudstersByCountry(true)
            print(result.first())
            val y = result.count()
            assert(y == 2)
        }
    }

}