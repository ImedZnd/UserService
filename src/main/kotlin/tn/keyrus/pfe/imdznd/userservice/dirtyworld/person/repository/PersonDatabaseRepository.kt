package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import reactor.core.publisher.Flux
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO.Companion.toDAO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class PersonDatabaseRepository(
    private val personReactiveRepository: PersonReactiveRepository,
) : PersonRepository {

    override suspend fun findPersonByID(id: Long) =
        personReactiveRepository
            .findById(id)
            .map { it.toPerson() }
            .filter { it.isRight }
            .map { it.get() }
            .map { Optional.of(it) }
            .awaitSingleOrNull()
            ?: Optional.empty()

    override fun findAllPerson() =
        findAllByCriteria { it.findAll() }

    override fun findAllPersonByBirthYear(year: Int) =
        findAllByCriteria { it.findAllByBirthYear(year) }

    override fun findAllPersonByState(state: Person.PersonState) =
        findAllByCriteria { it.findAllByState(state) }

    override fun findAllPersonByCountry(countryCode: String) =
        findAllByCriteria { it.findAllByCountryCode(countryCode) }

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
        findAllByCriteria { it.findAllByNumberOfFlagsGreaterThan(numberOfFlags) }

    override fun findAllPersonByNumberOfFlagsLessThan(numberOfFlags: Int) =
        findAllByCriteria { it.findAllByNumberOfFlagsLessThan(numberOfFlags) }

    override fun findAllPersonByIsFraudster(isFraudster: Boolean) =
        findAllByCriteria { it.findAllByFraudster(isFraudster) }

    override fun findAllPersonByBirthYearAndCountryCode(year: Int, countryCode: String) =
        findAllByCriteria { it.findAllByBirthYearAndCountryCode(year, countryCode) }

    override fun findAllPersonByBirthYearAndHasEmail(birthYear: Int, hasEmail: Boolean) =
        findAllByCriteria { it.findAllByBirthYearAndHasEmail(birthYear, hasEmail) }

    override fun findAllPersonByBirthYearBefore(birthYear: Int) =
        findAllByCriteria { it.findAllByBirthYearBefore(birthYear) }

    override fun findAllPersonByBirthYearAfter(birthYear: Int) =
        findAllByCriteria { it.findAllByBirthYearAfter(birthYear) }

    override fun findAllPersonByBirthYearBetween(startYear: Int, endYear: Int) =
        findAllByCriteria { it.findAllByBirthYearBetween(startYear, endYear) }

    override fun findAllPersonByCreatedDateAfter(creationDate: LocalDateTime) =
        findAllByCriteria { it.findAllByCreatedDateAfter(creationDate) }

    override fun findAllPersonByCreatedDateBefore(creationDate: LocalDateTime) =
        findAllByCriteria { it.findAllByCreatedDateBefore(creationDate) }

    override fun findAllPersonByTermsVersionBefore(termsVersion: LocalDate) =
        findAllByCriteria { it.findAllByTermsVersionBefore(termsVersion) }

    override fun findAllPersonByTermsVersionAfter(termsVersion: LocalDate) =
        findAllByCriteria { it.findAllByTermsVersionAfter(termsVersion) }

    override fun findAllPersonByFraudsterAndCountryCode(isFraudster: Boolean, country: String) =
        findAllByCriteria { it.findAllByFraudsterAndCountryCode(isFraudster, country) }

    override suspend fun countByBirthYear(birthYear: Int) =
        findAllPerson()
            .filter { it.birthYear == birthYear }
            .count()

    override suspend fun countAllPersonByState(state: Person.PersonState) =
        countPersonByCriteria { it.state == state }

    override suspend fun countAllPersonByTermsVersion(termsVersion: LocalDate) =
        countPersonByCriteria { it.termsVersion == termsVersion }

    override suspend fun countAllPersonByFraudster(isFraudster: Boolean) =
        countPersonByCriteria { it.fraudster == isFraudster }

    override suspend fun savePerson(person: Person): Either<PersonRepository.PersonRepositoryError, Person> =
        try {
            personReactiveRepository.save(person.toDAO())
                .map { it.toPerson() }
                .filter { it.isRight }
                .map { it.get() }
                .map { Either.right<PersonRepository.PersonRepositoryError, Person>(it) }
                .awaitSingleOrNull()
                ?: Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
        } catch (exception: Exception) {
            Either.left(PersonRepository.PersonRepositoryError.PersonRepositoryIOError)
        }

    override suspend fun updatePerson(person: Person): Either<PersonRepository.PersonRepositoryError, Person> =
        if (person.personId?.let { findPersonByID(it).isEmpty } == true)
            Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
        else {
            savePerson(person)
            Either.right(person)
        }

    override suspend fun deletePerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> =
        try {
            val doesPersonExist = checkPersonByIdExist(id)
            if (doesPersonExist.isRight)
                deletePersonAndReturnPerson(doesPersonExist.get())
            else Either.left(doesPersonExist.left)
        } catch (exception: Exception) {
            Either.left(PersonRepository.PersonRepositoryError.PersonRepositoryIOError)
        }

    private fun deletePersonAndReturnPerson(person: Person): Either<PersonRepository.PersonRepositoryError, Person> {
        personReactiveRepository.deleteById(person.getPersonId().get()).subscribe()
        return Either.right(person)
    }

    override suspend fun flagPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> =
        checkPersonByIdExist(id)
            .let { either ->
                if (either.isLeft)
                    Either.left(either.left)
                else {
                    val person = either.get()
                    Either.right(
                        updatePerson(
                            Person.of(
                                person.personId,
                                person.seqUser,
                                person.failedSignInAttempts,
                                person.birthYear,
                                person.countryCode,
                                person.createdDate,
                                person.termsVersion,
                                person.phoneCountry,
                                person.kyc,
                                person.state,
                                person.hasEmail,
                                person.numberOfFlags + 1,
                                person.fraudster,
                            ).get()
                        ).get()
                    )
                }
            }


    override suspend fun fraudPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val isFraudster = false
        checkPersonFraudster(id, isFraudster)
            .let { either ->
                return if (either.isLeft)
                    Either.left(either.left)
                else {
                    val person = either.get()
                    Either.right(
                        updatePerson(
                            Person.of(
                                person.personId,
                                person.seqUser,
                                person.failedSignInAttempts,
                                person.birthYear,
                                person.countryCode,
                                person.createdDate,
                                person.termsVersion,
                                person.phoneCountry,
                                person.kyc,
                                person.state,
                                person.hasEmail,
                                person.numberOfFlags,
                                true,
                            ).get()
                        ).get()
                    )
                }
            }

    }

    override suspend fun unFraudPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val isFraudster = true
        checkPersonFraudster(id, isFraudster)
            .let { either ->
                return if (either.isLeft)
                    Either.left(either.left)
                else {
                    val person = either.get()
                    Either.right(
                        updatePerson(
                            Person.of(
                                person.personId,
                                person.seqUser,
                                person.failedSignInAttempts,
                                person.birthYear,
                                person.countryCode,
                                person.createdDate,
                                person.termsVersion,
                                person.phoneCountry,
                                person.kyc,
                                person.state,
                                person.hasEmail,
                                person.numberOfFlags,
                                false,
                            ).get()
                        ).get()
                    )
                }
            }
    }

    private suspend fun checkPersonFraudster(
        id: Long,
        check: Boolean
    ): Either<PersonRepository.PersonRepositoryError, Person> {
        val person = checkPersonByIdExist(id)
        return if (person.isLeft)
            Either.left(person.left)
        else {
            checkAndFraudOrUnFraudPersonIfNotAlreadyIs(person.get(), check)
        }
    }

    private fun checkAndFraudOrUnFraudPersonIfNotAlreadyIs(
        person: Person,
        check: Boolean
    ): Either<PersonRepository.PersonRepositoryError, Person> {
        val personToFraudOrUnFraudAndNotAlreadyIs = person.fraudster xor check
        return if (personToFraudOrUnFraudAndNotAlreadyIs)
            Either.left(PersonRepository.PersonRepositoryError.PersonFraudsterRepositoryError)
        else Either.right(person)
    }

    private suspend fun checkPersonByIdExist(id: Long): Either<PersonRepository.PersonRepositoryError, Person> =
        findPersonByID(id)
            .map { Either.right<PersonRepository.PersonRepositoryError, Person>(it) }
            .orElse(Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError))

    override suspend fun countAllPerson() =
        countPersonByCriteria { true }

    override suspend fun countAllPersonByCountry(countryCode: String) =
        countPersonByCriteria { it.countryCode == countryCode }

    private fun findAllByCriteria(criteria: (PersonReactiveRepository) -> Flux<PersonDAO>) =
        criteria(personReactiveRepository)
            .asFlow()
            .map { it.toPerson() }
            .filter { it.isRight }
            .map { it.get() }

    private suspend fun countPersonByCriteria(predicate: (Person) -> Boolean) =
        findAllPerson()
            .filter(predicate)
            .count()
}