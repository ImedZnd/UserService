package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO.Companion.toDAO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year

class PersonDatabaseRepository(
    private val personReactiveRepository: PersonReactiveRepository
) : PersonRepository {

    override fun findAllPerson() =
        findAllByCriteria { it.findAll() }

    override fun findAllPersonByBirthYear(year: Year) =
        findAllByCriteria { it.findAllByBirthYear(year.value) }

    override fun findAllPersonByState(state: Person.PersonState) =
        findAllByCriteria { it.findAllByState(state) }

    override fun findAllPersonByCountry(countryCode: String) =
        findAllByCriteria { it.findAllByCountry(countryCode) }

    override fun findAllPersonByCreatedDateInRange(startDate: LocalDate, endDate: LocalDate) =
        findAllByCriteria {
            it.findAllByCreatedDateBetween(
                LocalDateTime.of(startDate, LocalTime.now()),
                LocalDateTime.of(endDate, LocalTime.now())
            )
        }

    override fun findAllPersonByTermsVersionBetween(startDate: LocalDate, endDate: LocalDate) =
        findAllByCriteria { it.findAllByTermsVersionBetween(startDate, endDate) }

    override fun findAllPersonByTermsVersion(termsVersion: LocalDate) =
        findAllByCriteria { it.findAllByTermsVersion(termsVersion) }

    override fun findAllPersonByPhoneCountry(phoneCountry: String) =
        findAllByCriteria { it.findAllByPhoneCountryContaining(phoneCountry) }

    override fun findAllPersonByKYC(kyc: Person.PersonKYC) =
        findAllByCriteria { it.findAllByKyc(kyc) }

    override fun findAllPersonByHasEmail(hasEmail: Boolean) =
        findAllByCriteria { it.findAllByHasEmail(hasEmail) }

    override fun findAllPersonByNumberOfFlags(numberOfFlags: Int) =
        findAllByCriteria { it.findAllByNumberOfFlags(numberOfFlags) }

    override fun findAllPersonByNumberOfFlagsGreaterThan(numberOfFlags: Int) =
        findAllByCriteria { it.findAllByNumberOfFlagsGreaterThan(numberOfFlags)}

    override fun findAllPersonByNumberOfFlagsLessThan(numberOfFlags: Int) =
        findAllByCriteria { it.findAllByNumberOfFlagsLessThan(numberOfFlags)}

    override fun findAllPersonGroupByCountry(countryCode: String) =
        findAllByCriteria { it.findAllByCountry(countryCode) }

    override fun findAllPersonByIsFraudster(isFraudster: Boolean) =
        findAllByCriteria { it.findAllByFraudster(isFraudster) }

    override fun findAllPersonByBirthYearAndCountryCode(year: Year, countryCode: String) =
        findAllByCriteria { it.findAllByBirthYearAndCountryCode(year.value,countryCode) }

    override fun findAllPersonByBirthYearAndHasEmail(birthYear: Year, hasEmail: Boolean) =
        findAllByCriteria { it.findAllByBirthYearAndHasEmail(birthYear.value,hasEmail) }

    override fun findAllPersonByBirthYearBefore(birthYear: Year) =
        findAllByCriteria { it.findAllByBirthYearBefore(birthYear.value) }

    override fun findAllPersonByBirthYearAfter(birthYear: Year) =
        findAllByCriteria { it.findAllByBirthYearAfter(birthYear.value) }

    override fun findAllPersonByBirthYearBetween(startYear: Year, endYear: Year)=
        findAllByCriteria { it.findAllByBirthYearBetween(startYear.value,endYear.value) }

    override fun findAllPersonByCreatedDateAfter(creationDate: LocalDateTime)=
        findAllByCriteria { it.findAllByCreatedDateAfter(creationDate) }

    override fun findAllPersonByCreatedDateBefore(creationDate: LocalDateTime)=
        findAllByCriteria { it.findAllByCreatedDateBefore(creationDate) }

    override fun findAllPersonByTermsVersionBefore(termsVersion: LocalDate)=
        findAllByCriteria { it.findAllByTermsVersionBefore(termsVersion) }

    override fun findAllPersonByTermsVersionAfter(termsVersion: LocalDate)=
        findAllByCriteria { it.findAllByTermsVersionAfter(termsVersion) }

    override fun findAllPersonByFailedSignInAttemptsGreaterThan(failedSignInAttempts: Int)=
        findAllByCriteria { it.findAllByFailedSignInAttemptsGreaterThan(failedSignInAttempts) }

    override fun findAllPersonByFailedSignInAttemptsLessThan(failedSignInAttempts: Int)=
        findAllByCriteria { it.findAllByFailedSignInAttemptsLessThan(failedSignInAttempts) }

    override fun findAllPersonByFraudsterAndCountryCode(isFraudster: Boolean, country: String)=
        findAllByCriteria { it.findAllByFraudsterAndCountryCode(isFraudster,country) }

    override fun countAllPersonByBirthYear(birthYear: Year)=
        countPeronByCriteria {it.countByBirthYear(birthYear.value) }

    override fun countAllPersonByState(state: Person.PersonState)=
        countPeronByCriteria {it.countByState(state) }

    override fun countAllPersonByTermsVersion(termsVersion: LocalDate)=
        countPeronByCriteria {it.countByTermsVersion(termsVersion) }

    override fun countAllPersonByFraudster(isFraudster: Boolean)=
        countPeronByCriteria {it.countByFraudster(isFraudster) }

    override suspend fun savePerson(person: Person): Either<PersonRepository.PersonRepositoryIOError, Person> =
        try {
        personReactiveRepository.save(person.toDAO())
            .map { it.toPerson() }
            .filter { it.isRight }
            .map { it.get() }
            .map { Either.right<PersonRepository.PersonRepositoryIOError, Person>(it) }
            .awaitSingleOrNull()
            ?: Either.left(PersonRepository.PersonRepositoryIOError)
    } catch (exception: Exception) {
        Either.left(PersonRepository.PersonRepositoryIOError)
    }

    override suspend fun countAllPerson() =
        findAllPerson().count()

    override suspend fun countAllPersonByFraudsters(isFraudster: Boolean)=
        findAllPerson()
            .filter { it.fraudster == isFraudster}
            .count()

    override suspend fun countAllPersonByCountry(countryCode: String)=
        findAllPerson()
            .filter { it.countryCode == countryCode }
            .count()

    private fun countPeronByCriteria(criteria: (PersonReactiveRepository) -> Mono<Long>): Mono<Long> =
        criteria(personReactiveRepository)

    private fun findAllByCriteria(criteria: (PersonReactiveRepository) -> Flux<PersonDAO>): Flow<Person> =
        criteria(personReactiveRepository)
            .asFlow()
            .map { it.toPerson() }
            .filter { it.isRight }
            .map { it.get() }
}