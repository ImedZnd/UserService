package tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

interface PersonRepository {

    suspend fun findPersonByID(id: Long): Optional<Person>
    fun findAllPerson(): Flow<Person>
    fun findAllPersonByBirthYear(year: Int): Flow<Person>
    fun findAllPersonByState(state: Person.PersonState): Flow<Person>
    fun findAllPersonByCountry(countryCode: String): Flow<Person>
    fun findAllPersonByCreatedDateInRange(startDate: LocalDate, endDate: LocalDate = startDate): Flow<Person>
    fun findAllPersonByTermsVersionBetween(startDate: LocalDate, endDate: LocalDate = startDate): Flow<Person>
    fun findAllPersonByPhoneCountry(phoneCountry: String): Flow<Person>
    fun findAllPersonByKYC(kyc: Person.PersonKYC): Flow<Person>
    fun findAllPersonByHasEmail(hasEmail: Boolean): Flow<Person>
    fun findAllPersonByNumberOfFlags(numberOfFlags: Int): Flow<Person>
    fun findAllPersonByNumberOfFlagsGreaterThan(numberOfFlags: Int): Flow<Person>
    fun findAllPersonByNumberOfFlagsLessThan(numberOfFlags: Int): Flow<Person>
    fun findAllPersonByIsFraudster(isFraudster: Boolean): Flow<Person>
    fun findAllPersonByBirthYearAndCountryCode(year: Int, countryCode: String): Flow<Person>
    fun findAllPersonByBirthYearAndHasEmail(birthYear: Int, hasEmail: Boolean): Flow<Person>
    fun findAllPersonByBirthYearBefore(birthYear: Int): Flow<Person>
    fun findAllPersonByBirthYearAfter(birthYear: Int): Flow<Person>
    fun findAllPersonByBirthYearBetween(startYear: Int, endYear: Int): Flow<Person>
    fun findAllPersonByCreatedDateAfter(creationDate: LocalDateTime): Flow<Person>
    fun findAllPersonByCreatedDateBefore(creationDate: LocalDateTime): Flow<Person>
    fun findAllPersonByTermsVersion(termsVersion: LocalDate): Flow<Person>
    fun findAllPersonByTermsVersionBefore(termsVersion: LocalDate): Flow<Person>
    fun findAllPersonByTermsVersionAfter(termsVersion: LocalDate): Flow<Person>
    fun findAllPersonByFraudsterAndCountryCode(isFraudster: Boolean, country: String): Flow<Person>
    suspend fun countByBirthYear(birthYear: Int): Int
    suspend fun countAllPersonByState(state: Person.PersonState): Int
    suspend fun countAllPersonByTermsVersion(termsVersion: LocalDate): Int
    suspend fun countAllPersonByFraudster(isFraudster: Boolean): Int
    suspend fun countAllPerson(): Int
    suspend fun countAllPersonByCountry(countryCode: String): Int
    suspend fun savePerson(person: Person): Either<PersonRepositoryError, Person>
    suspend fun updatePerson(person: Person): Either<PersonRepositoryError, Person>
    suspend fun deletePerson(id: Long): Either<PersonRepositoryError, Person>
    suspend fun flagPerson(id: Long): Either<PersonRepositoryError, Person>
    suspend fun fraudPerson(id: Long): Either<PersonRepositoryError, Person>
    suspend fun unFraudPerson(id: Long): Either<PersonRepositoryError, Person>

    sealed interface PersonRepositoryError {
        object PersonRepositoryIOError : PersonRepositoryError
        object PersonNotExistPersonRepositoryError : PersonRepositoryError
        object PersonFraudsterRepositoryError : PersonRepositoryError
    }

}