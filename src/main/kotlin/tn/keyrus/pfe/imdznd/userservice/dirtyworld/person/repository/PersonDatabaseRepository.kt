package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
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

    override suspend fun countByBirthYear(birthYear: Year) =
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
        if ((person.personId == null) or (person.personId?.let { findPersonByID(it).isEmpty } == true))
            Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
        else {
            savePerson(person)
            Either.right(person)
        }

    override suspend fun deletePerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        return try {
            val doesPersonExist = findPersonByID(id)
            when {
                doesPersonExist.isPresent -> {
                    personReactiveRepository.deleteById(id).subscribe()
                    return Either.right(doesPersonExist.get())
                }
                else -> return Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
            }
        } catch (exception: Exception) {
            Either.left(PersonRepository.PersonRepositoryError.PersonRepositoryIOError)
        }
    }

    override suspend fun flagPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val personToFlag = checkPersonByIdExist(id)
        if (personToFlag.isLeft)
            return Either.left(personToFlag.left)
        val person = personToFlag.get()
        return Either.right(
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
                    person.numberOfFlags +1,
                    person.fraudster,
                ).get()
            ).get()
        )    }

    override suspend fun fraudPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val isFraudster = false
        val personToFraud = checkPersonFraudster(id,isFraudster)
        if (personToFraud.isLeft)
            return Either.left(personToFraud.left)
        val person = personToFraud.get()
        return Either.right(
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

    override suspend fun unFraudPerson(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val isFraudster = true
        val personToUnFraud = checkPersonFraudster(id,isFraudster)
        if (personToUnFraud.isLeft)
            return Either.left(personToUnFraud.left)
        val person = personToUnFraud.get()
        return Either.right(
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

    private suspend fun checkPersonFraudster(id: Long,check:Boolean): Either<PersonRepository.PersonRepositoryError, Person> {
        val person = checkPersonByIdExist(id)
        if (person.isLeft)
            return Either.left(person.left)
        if (person.get().fraudster xor check)
            return Either.left(PersonRepository.PersonRepositoryError.PersonFraudsterRepositoryError)
        return Either.right(person.get())
    }

    private suspend fun checkPersonByIdExist(id: Long): Either<PersonRepository.PersonRepositoryError, Person> {
        val person = findPersonByID(id)
        if (person.isEmpty)
            return Either.left(PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError)
        return Either.right(person.get())
    }

    override fun publishSavePerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.save?.exchange ?: "",
            personQueueSetting.save?.routingkey ?: ""
        )
    }

    override fun publishUpdatePerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.update?.exchange ?: "",
            personQueueSetting.update?.routingkey ?: ""
        )
    }

    override fun publishDeletePerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.delete?.exchange ?: "",
            personQueueSetting.delete?.routingkey ?: ""
        )
    }

    override fun publishFlagPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.flag?.exchange ?: "",
            personQueueSetting.flag?.routingkey ?: ""
        )
    }

    override fun publishFraudPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.fraud?.exchange ?: "",
            personQueueSetting.fraud?.routingkey ?: ""
        )
    }

    override fun publishUnFraudPerson(id: Long) {
        publishEvent(
            id.toString(),
            personQueueSetting.unfraud?.exchange ?: "",
            personQueueSetting.unfraud?.routingkey ?: ""
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

    private fun findAllByCriteria(criteria: (PersonReactiveRepository) -> Flux<PersonDAO>) =
        criteria(personReactiveRepository)
            .asFlow()
            .map { it.toPerson() }
            .filter { it.isRight }
            .map { it.get() }

    private suspend fun countPersonByCriteria(predicate: (Person) -> Boolean) =
        findAllPerson()
            .filter { predicate(it) }
            .count()
}