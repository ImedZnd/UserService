package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO.Companion.toDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.setting.PersonQueueSetting
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.util.*
class PersonDatabaseRepository(
    private val personReactiveRepository: PersonReactiveRepository,
    @Autowired private val rabbitTemplate: RabbitTemplate,
    private val personQueueSetting: PersonQueueSetting
) : PersonRepository {

    override fun findPersonByID(id: Long): Mono<Optional<Person>> {
        return personReactiveRepository.findById(id)
            .map { it.toPerson() }
            .filter { it.isRight }
            .map { it.get() }
            .map { Optional.of(it) }
            .switchIfEmpty(Mono.just(Optional.empty()))
    }

    override fun findAllPerson() =
        findAllByCriteria { it.findAll() }

    override fun findAllPersonByBirthYear(year: Year) =
        findAllByCriteria { it.findAllByBirthYear(year.value) }

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

    override fun findAllPersonByBirthYearAndCountryCode(year: Year, countryCode: String) =
        findAllByCriteria { it.findAllByBirthYearAndCountryCode(year.value, countryCode) }

    override fun findAllPersonByBirthYearAndHasEmail(birthYear: Year, hasEmail: Boolean) =
        findAllByCriteria { it.findAllByBirthYearAndHasEmail(birthYear.value, hasEmail) }

    override fun findAllPersonByBirthYearBefore(birthYear: Year) =
        findAllByCriteria { it.findAllByBirthYearBefore(birthYear.value) }

    override fun findAllPersonByBirthYearAfter(birthYear: Year) =
        findAllByCriteria { it.findAllByBirthYearAfter(birthYear.value) }

    override fun findAllPersonByBirthYearBetween(startYear: Year, endYear: Year) =
        findAllByCriteria { it.findAllByBirthYearBetween(startYear.value, endYear.value) }

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

    override suspend fun countByBirthYear(birthYear: Year): Long {
        return findAllPerson()
            .filter { it.birthYear == birthYear }
            .count()
            .toLong()
    }

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
        if ((person.personId == null) or (person.personId?.let { findPersonByID(it).awaitSingle().isEmpty } == true))
            Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
        else {
            savePerson(person)
            Either.right(person)
        }

    override suspend fun deletePerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        return try {
            val doesPersonExist = findPersonByID(id)
                .filter { it.isPresent }
                .map { Either.right<PersonRepository.PersonRepositoryError, Person>(it.get()) }
                .awaitSingleOrNull()
                ?: Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
            personReactiveRepository.deleteById(id).subscribe()
            Either.right(doesPersonExist.get())
        } catch (exception: java.lang.Exception) {
            Either.left(PersonRepository.PersonRepositoryError.PersonRepositoryIOError)
        }
    }

    override suspend fun flagPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val personToUnFlag = checkIdOfFraudster(id)
            if (personToUnFlag.isLeft)
                return Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
            val person = personToUnFlag.get()
            val personToUpdate = Person.of(
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
            updatePerson(
                personToUpdate
            )
            return Either.right(personToUpdate)
    }

    override suspend fun fraudPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val personToUnFraud = checkIdOfFraudster(id)
        if (personToUnFraud.isLeft){
            println(personToUnFraud.isLeft)
            return Either.left(personToUnFraud.left)
        }
        if (personToUnFraud.get().fraudster){
            println(personToUnFraud.get().fraudster)
            return Either.left(PersonRepository.PersonRepositoryError.PersonAlreadyFraudsterRepositoryError)
        }
        val person = personToUnFraud.get()
        val personToUpdate = Person.of(
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
        updatePerson(
            personToUpdate
        )
        return Either.right(personToUpdate)
    }

    override suspend fun unFraudPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val personToUnFraud = checkIdOfFraudster(id)
            if (personToUnFraud.isLeft)
                return Either.left(personToUnFraud.left)
            if (!personToUnFraud.get().fraudster)
                return Either.left(PersonRepository.PersonRepositoryError.PersonAlreadyFraudsterRepositoryError)
            val person = personToUnFraud.get()
            val personToUpdate = Person.of(
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
            updatePerson(
                personToUpdate
            )
            return Either.right(personToUpdate)
    }

    private suspend fun checkIdOfFraudster(id: Long):Either<PersonRepository.PersonRepositoryError, Person> {
        val person = findPersonByID(id)
            .awaitSingleOrNull()
        if (person != null) {
            if (person.isEmpty)
                return Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
            return Either.right(person.get())
        }
        return Either.left(PersonRepository.PersonRepositoryError.PersonRepositoryIOError)
    }

    override fun publishSavePerson(id: Long) {
        println(personQueueSetting.toString())
        publishEvent(
            id.toString(),
            personQueueSetting.save?.exchange?:"",
            personQueueSetting.save?.routingkey?:""
        )
    }

    override fun publishUpdatePerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.update?.exchange?:"",
            personQueueSetting.update?.routingkey?:""
        )
    }

    override fun publishDeletePerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.delete?.exchange?:"",
            personQueueSetting.delete?.routingkey?:""
        )
    }

    override fun publishFlagPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.flag?.exchange?:"",
            personQueueSetting.flag?.routingkey?:""
        )
    }

    override fun publishFraudPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.fraud?.exchange?:"",
            personQueueSetting.fraud?.routingkey?:""
        )
    }

    override fun publishUnFraudPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.unfraud?.exchange?:"",
            personQueueSetting.unfraud?.routingkey?:""
        )
    }

    private fun publishEvent(
        personId: String,
        exchange: String,
        routingKey: String
    ) {
        rabbitTemplate.convertAndSend(
            exchange,
            routingKey,
            personId
        )
    }

    override suspend fun countAllPerson() =
        countPersonByCriteria { true }

    override suspend fun countAllPersonByCountry(countryCode: String) =
        countPersonByCriteria { it.countryCode == countryCode }

    private fun findAllByCriteria(criteria: (PersonReactiveRepository) -> Flux<PersonDAO>): Flow<Person> {
        return criteria(personReactiveRepository)
            .asFlow()
            .map { it.toPerson() }
            .filter { it.isRight }
            .map { it.get() }
    }

    private suspend fun countPersonByCriteria(predicate: (Person) -> Boolean): Long {
        return findAllPerson()
            .filter { predicate(it) }
            .count()
            .toLong()
    }
}