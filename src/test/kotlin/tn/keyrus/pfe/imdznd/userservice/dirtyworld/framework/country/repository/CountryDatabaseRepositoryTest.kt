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
}