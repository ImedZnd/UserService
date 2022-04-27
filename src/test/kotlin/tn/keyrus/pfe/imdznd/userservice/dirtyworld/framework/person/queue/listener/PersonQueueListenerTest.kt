package tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.person.queue.listener

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.framework.initializer.Initializer
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonReactiveRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PersonQueueListenerTest(
    @Autowired private val rabbitTemplate: RabbitTemplate,
    @Autowired private val rabbitAdmin: RabbitAdmin,
    @Autowired private val personReactiveRepository: PersonReactiveRepository,
    @Autowired private val personService: PersonService,
    @Autowired private val countryRepository: CountryRepository,
    ){
    @BeforeAll
    fun beforeAll() {
        runBlocking {
            personReactiveRepository.deleteAll().awaitSingleOrNull()
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
            rabbitAdmin.purgeQueue("flagpersonqueue")
            rabbitAdmin.purgeQueue("savepersonqueue")
            rabbitAdmin.purgeQueue("updatepersonqueue")
            rabbitAdmin.purgeQueue("deletepersonqueue")
        }
    }

    @Test
    fun `flag user consumer `() {
        runBlocking {
            withContext(Dispatchers.IO) {
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
                countryRepository
                    .saveCountry(
                        countrySave
                    )
                val savedPerson = personService.savePerson(resultPerson).get()
                val resultPersons =
                    personService.getAllPersons().count()
                    rabbitTemplate.convertAndSend(
                        "flagpersontransactionexchange",
                        "flagpersonransactionroutingkey",
                        savedPerson.personId.toString()
                    )

                Thread.sleep(1000)
                val detailRes = personService.getAllPersons()
                val per = detailRes.first()
                    assert(resultPersons == 1)
                    println(per.numberOfFlags)
                    println(resultPerson.numberOfFlags)
                    assert(per.numberOfFlags == resultPerson.numberOfFlags+1)
            }
        }
    }
}