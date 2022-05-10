package tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service

import io.vavr.control.Either
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

class PersonService(
    private val personDatabaseRepository: PersonRepository
) {

    suspend fun getPersonByID(id: Long) =
        personDatabaseRepository.findPersonByID(id)

    fun getAllPersons() =
        personDatabaseRepository.findAllPerson()

    fun getAllPersonsByBirthYear(birthYear: Year) =
        personDatabaseRepository.findAllPersonByBirthYear(birthYear)

    fun getAllPersonsByState(state: Person.PersonState) =
        personDatabaseRepository.findAllPersonByState(state)

    fun getAllPersonByCountry(countryCode: String) =
        personDatabaseRepository.findAllPersonByCountry(countryCode)

    fun getAllPersonByCreatedDateInRange(startDate: LocalDate, endDate: LocalDate = startDate.plusDays(1)) =
        personDatabaseRepository.findAllPersonByCreatedDateInRange(startDate, endDate)

    fun getAllPersonByTermsVersionBetween(startDate: LocalDate, endDate: LocalDate = startDate.plusDays(1)) =
        personDatabaseRepository.findAllPersonByTermsVersionBetween(startDate, endDate)

    fun getAllPersonByPhoneCountry(phoneCountry: String) =
        personDatabaseRepository.findAllPersonByPhoneCountry(phoneCountry)

    fun getAllPersonByKYC(kyc: Person.PersonKYC) =
        personDatabaseRepository.findAllPersonByKYC(kyc)

    fun getAllPersonByHasEmail(hasEmail: Boolean) =
        personDatabaseRepository.findAllPersonByHasEmail(hasEmail)

    fun getAllPersonByNumberOfFlags(numberOfFlags: Int) =
        personDatabaseRepository.findAllPersonByNumberOfFlags(numberOfFlags)

    fun getAllPersonByNumberOfFlagsGreaterThan(numberOfFlags: Int) =
        personDatabaseRepository.findAllPersonByNumberOfFlagsGreaterThan(numberOfFlags)

    fun getAllPersonByNumberOfFlagsLessThan(numberOfFlags: Int) =
        personDatabaseRepository.findAllPersonByNumberOfFlagsLessThan(numberOfFlags)

    fun getAllPersonByIsFraudster(isFraudster: Boolean) =
        personDatabaseRepository.findAllPersonByIsFraudster(isFraudster)

    fun getAllPersonByBirthYearAndCountryCode(year: Year, countryCode: String) =
        personDatabaseRepository.findAllPersonByBirthYearAndCountryCode(year, countryCode)

    fun getAllPersonByBirthYearAndHasEmail(birthYear: Year, hasEmail: Boolean) =
        personDatabaseRepository.findAllPersonByBirthYearAndHasEmail(birthYear, hasEmail)

    fun getAllPersonByBirthYearBefore(birthYear: Year) =
        personDatabaseRepository.findAllPersonByBirthYearBefore(birthYear)

    fun getAllPersonByBirthYearAfter(birthYear: Year) =
        personDatabaseRepository.findAllPersonByBirthYearAfter(birthYear)

    fun getAllPersonByBirthYearBetween(startYear: Year, endYear: Year) =
        personDatabaseRepository.findAllPersonByBirthYearBetween(startYear, endYear)

    fun getAllPersonByCreatedDateAfter(creationDate: LocalDateTime) =
        personDatabaseRepository.findAllPersonByCreatedDateAfter(creationDate)

    fun getAllPersonByCreatedDateBefore(creationDate: LocalDateTime) =
        personDatabaseRepository.findAllPersonByCreatedDateBefore(creationDate)

    fun getAllPersonByTermsVersion(termsVersion: LocalDate) =
        personDatabaseRepository.findAllPersonByTermsVersion(termsVersion)

    fun getAllPersonByTermsVersionBefore(termsVersion: LocalDate) =
        personDatabaseRepository.findAllPersonByTermsVersionBefore(termsVersion)

    fun getAllPersonByTermsVersionAfter(termsVersion: LocalDate) =
        personDatabaseRepository.findAllPersonByTermsVersionAfter(termsVersion)

    fun getAllPersonByFraudsterAndCountryCode(isFraudster: Boolean, country: String) =
        personDatabaseRepository.findAllPersonByFraudsterAndCountryCode(isFraudster, country)

    suspend fun countByBirthYear(birthYear: Year) =
        personDatabaseRepository.countByBirthYear(birthYear)

    suspend fun countAllPersonByState(state: Person.PersonState) =
        personDatabaseRepository.countAllPersonByState(state)

    suspend fun countAllPersonByTermsVersion(termsVersion: LocalDate) =
        personDatabaseRepository.countAllPersonByTermsVersion(termsVersion)

    suspend fun countAllPersonByFraudster(isFraudster: Boolean) =
        personDatabaseRepository.countAllPersonByFraudster(isFraudster)

    suspend fun countAllPerson() =
        personDatabaseRepository.countAllPerson()

    suspend fun countAllPersonByCountry(countryCode: String) =
        personDatabaseRepository.countAllPersonByCountry(countryCode)

    suspend fun savePerson(person: Person): Either<PersonServiceError, Person> {
        val personSaved = personDatabaseRepository.savePerson(person)
        return if (personSaved.isLeft)
            Either.left(changeRepositoryErrorToServiceError(personSaved.left))
        else {
            personSaved.get().personId?.let { it1 -> personDatabaseRepository.publishSavePerson(it1) }
            Either.right(personSaved.get())
        }
    }

    suspend fun updatePerson(person: Person): Either<PersonServiceError, Person> =
        personDatabaseRepository.updatePerson(person)
            .also { person.personId?.let { it1 -> personDatabaseRepository.publishUpdatePerson(it1) } }
            .mapLeft { changeRepositoryErrorToServiceError(it) }

    suspend fun deletePerson(id: Long): Either<PersonServiceError, Person> =
        personDatabaseRepository.deletePerson(id)
            .also { personDatabaseRepository.publishDeletePerson(id) }
            .mapLeft { changeRepositoryErrorToServiceError(it) }

    suspend fun flagPerson(id: Long): Either<PersonServiceError, Person> =
        personDatabaseRepository.flagPerson(id)
            .also { personDatabaseRepository.publishFlagPerson(id) }
            .mapLeft { changeRepositoryErrorToServiceError(it) }

    suspend fun fraudPerson(id: Long): Either<PersonServiceError, Person> =
        personDatabaseRepository.fraudPerson(id)
            .also { personDatabaseRepository.publishFraudPerson(id) }
            .mapLeft { changeRepositoryErrorToServiceError(it) }

    suspend fun unFraudPerson(id: Long): Either<PersonServiceError, Person> =
        personDatabaseRepository.unFraudPerson(id)
            .also { personDatabaseRepository.publishUnFraudPerson(id) }
            .mapLeft { changeRepositoryErrorToServiceError(it) }

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