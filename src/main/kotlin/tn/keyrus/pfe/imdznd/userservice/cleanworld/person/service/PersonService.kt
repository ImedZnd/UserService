package tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service

import io.vavr.control.Either
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.publisher.PersonQueuePublisher
import java.time.LocalDate
import java.time.LocalDateTime

class PersonService(
    private val personRepository: PersonRepository,
    private val personQueuePublisher: PersonQueuePublisher,
) {

    suspend fun getPersonByID(id: Long) =
        personRepository.findPersonByID(id)

    fun getAllPersons() =
        personRepository.findAllPerson()

    fun getAllPersonsByBirthYear(birthYear: Int) =
        personRepository.findAllPersonByBirthYear(birthYear)

    fun getAllPersonsByState(state: Person.PersonState) =
        personRepository.findAllPersonByState(state)

    fun getAllPersonByCountry(countryCode: String) =
        personRepository.findAllPersonByCountry(countryCode)

    fun getAllPersonByCreatedDateInRange(startDate: LocalDate, endDate: LocalDate = startDate.plusDays(1)) =
        personRepository.findAllPersonByCreatedDateInRange(startDate, endDate)

    fun getAllPersonByTermsVersionBetween(startDate: LocalDate, endDate: LocalDate = startDate.plusDays(1)) =
        personRepository.findAllPersonByTermsVersionBetween(startDate, endDate)

    fun getAllPersonByPhoneCountry(phoneCountry: String) =
        personRepository.findAllPersonByPhoneCountry(phoneCountry)

    fun getAllPersonByKYC(kyc: Person.PersonKYC) =
        personRepository.findAllPersonByKYC(kyc)

    fun getAllPersonByHasEmail(hasEmail: Boolean) =
        personRepository.findAllPersonByHasEmail(hasEmail)

    fun getAllPersonByNumberOfFlags(numberOfFlags: Int) =
        personRepository.findAllPersonByNumberOfFlags(numberOfFlags)

    fun getAllPersonByNumberOfFlagsGreaterThan(numberOfFlags: Int) =
        personRepository.findAllPersonByNumberOfFlagsGreaterThan(numberOfFlags)

    fun getAllPersonByNumberOfFlagsLessThan(numberOfFlags: Int) =
        personRepository.findAllPersonByNumberOfFlagsLessThan(numberOfFlags)

    fun getAllPersonByIsFraudster(isFraudster: Boolean) =
        personRepository.findAllPersonByIsFraudster(isFraudster)

    fun getAllPersonByBirthYearAndCountryCode(year: Int, countryCode: String) =
        personRepository.findAllPersonByBirthYearAndCountryCode(year, countryCode)

    fun getAllPersonByBirthYearAndHasEmail(birthYear: Int, hasEmail: Boolean) =
        personRepository.findAllPersonByBirthYearAndHasEmail(birthYear, hasEmail)

    fun getAllPersonByBirthYearBefore(birthYear: Int) =
        personRepository.findAllPersonByBirthYearBefore(birthYear)

    fun getAllPersonByBirthYearAfter(birthYear: Int) =
        personRepository.findAllPersonByBirthYearAfter(birthYear)

    fun getAllPersonByBirthYearBetween(startYear: Int, endYear: Int) =
        personRepository.findAllPersonByBirthYearBetween(startYear, endYear)

    fun getAllPersonByCreatedDateAfter(creationDate: LocalDateTime) =
        personRepository.findAllPersonByCreatedDateAfter(creationDate)

    fun getAllPersonByCreatedDateBefore(creationDate: LocalDateTime) =
        personRepository.findAllPersonByCreatedDateBefore(creationDate)

    fun getAllPersonByTermsVersion(termsVersion: LocalDate) =
        personRepository.findAllPersonByTermsVersion(termsVersion)

    fun getAllPersonByTermsVersionBefore(termsVersion: LocalDate) =
        personRepository.findAllPersonByTermsVersionBefore(termsVersion)

    fun getAllPersonByTermsVersionAfter(termsVersion: LocalDate) =
        personRepository.findAllPersonByTermsVersionAfter(termsVersion)

    fun getAllPersonByFraudsterAndCountryCode(isFraudster: Boolean, country: String) =
        personRepository.findAllPersonByFraudsterAndCountryCode(isFraudster, country)

    suspend fun countByBirthYear(birthYear: Int) =
        personRepository.countByBirthYear(birthYear)

    suspend fun countAllPersonByState(state: Person.PersonState) =
        personRepository.countAllPersonByState(state)

    suspend fun countAllPersonByTermsVersion(termsVersion: LocalDate) =
        personRepository.countAllPersonByTermsVersion(termsVersion)

    suspend fun countAllPersonByFraudster(isFraudster: Boolean) =
        personRepository.countAllPersonByFraudster(isFraudster)

    suspend fun countAllPerson() =
        personRepository.countAllPerson()

    suspend fun countAllPersonByCountry(countryCode: String) =
        personRepository.countAllPersonByCountry(countryCode)

    suspend fun savePerson(person: Person): Either<PersonServiceError, Person> =
        personRepository.savePerson(person)
            .let {
                if (it.isLeft)
                    Either.left(changeRepositoryErrorToServiceError(it.left))
                else
                    publishSavedPersonAndReturnPerson(it.get())
            }

    private fun publishSavedPersonAndReturnPerson(person: Person): Either<PersonServiceError, Person> {
        person.getPersonId().ifPresent {
            personQueuePublisher.publishSavePerson(it)
        }
        return Either.right(person)
    }

    suspend fun updatePerson(person: Person): Either<PersonServiceError, Person> =
        personRepository.updatePerson(person)
            .let {
                if (it.isLeft)
                    Either.left(changeRepositoryErrorToServiceError(it.left))
                else
                    publishUpdatePersonAndReturnPerson(it.get())
            }

    private fun publishUpdatePersonAndReturnPerson(person: Person): Either<PersonServiceError, Person> {
        person.getPersonId().ifPresent {
            personQueuePublisher.publishUpdatePerson(it)
        }
        return Either.right(person)
    }

    suspend fun deletePerson(id: Long): Either<PersonServiceError, Person> =
        personRepository.deletePerson(id)
            .let {
                if (it.isLeft)
                    Either.left(changeRepositoryErrorToServiceError(it.left))
                else
                    publishDeletePersonAndReturnPerson(it.get())
            }

    private fun publishDeletePersonAndReturnPerson(person: Person): Either<PersonServiceError, Person> {
        person.getPersonId().ifPresent {
            personQueuePublisher.publishDeletePerson(it)
        }
        return Either.right(person)
    }

    suspend fun flagPerson(id: Long): Either<PersonServiceError, Person> =
        personRepository.flagPerson(id)
            .let {
                if (it.isLeft)
                    Either.left(changeRepositoryErrorToServiceError(it.left))
                else
                    publishFlagPersonAndReturnPerson(it.get())
            }

    private fun publishFlagPersonAndReturnPerson(person: Person): Either<PersonServiceError, Person> {
        person.getPersonId().ifPresent {
            personQueuePublisher.publishFlagPerson(it)
        }
        return Either.right(person)
    }

    suspend fun fraudPerson(id: Long): Either<PersonServiceError, Person> =
        personRepository.fraudPerson(id)
            .let {
                if (it.isLeft)
                    Either.left(changeRepositoryErrorToServiceError(it.left))
                else
                    publishFraudPersonAndReturnPerson(it.get())
            }

    private fun publishFraudPersonAndReturnPerson(person: Person): Either<PersonServiceError, Person> {
        person.getPersonId().ifPresent {
            personQueuePublisher.publishFraudPerson(it)
        }
        return Either.right(person)
    }

    suspend fun unFraudPerson(id: Long): Either<PersonServiceError, Person> =
        personRepository.unFraudPerson(id)
            .let {
                if (it.isLeft)
                    Either.left(changeRepositoryErrorToServiceError(it.left))
                else
                    publishUnFraudPersonAndReturnPerson(it.get())
            }

    private fun publishUnFraudPersonAndReturnPerson(person: Person): Either<PersonServiceError, Person> {
        person.getPersonId().ifPresent {
            personQueuePublisher.publishUnFraudPerson(it)
        }
        return Either.right(person)
    }

    private fun changeRepositoryErrorToServiceError(personRepositoryError: PersonRepository.PersonRepositoryError): PersonServiceError =
        when (personRepositoryError) {
            is PersonRepository.PersonRepositoryError.PersonRepositoryIOError -> PersonServiceError.PersonServiceIOError
            is PersonRepository.PersonRepositoryError.PersonNotExistPersonRepositoryError -> PersonServiceError.PersonServicePersonNotExistError
            is PersonRepository.PersonRepositoryError.PersonFraudsterRepositoryError -> PersonServiceError.PersonFraudsterServiceError
        }

    sealed interface PersonServiceError {
        object PersonServiceIOError : PersonServiceError
        object PersonServicePersonNotExistError : PersonServiceError
        object PersonFraudsterServiceError : PersonServiceError
    }

}