package tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.*

interface PersonRepository {

    fun findPersonByID(id:UUID): Mono<Optional<Person>>
    fun findAllPerson(): Flow<Person>
    fun findAllPersonByBirthYear(year: Year): Flow<Person>
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
    fun findAllPersonByBirthYearAndCountryCode(year: Year,countryCode: String): Flow<Person>
    fun findAllPersonByBirthYearAndHasEmail(birthYear: Year, hasEmail: Boolean): Flow<Person>
    fun findAllPersonByBirthYearBefore(birthYear: Year): Flow<Person>
    fun findAllPersonByBirthYearAfter(birthYear: Year): Flow<Person>
    fun findAllPersonByBirthYearBetween(startYear: Year, endYear: Year): Flow<Person>
    fun findAllPersonByCreatedDateAfter(creationDate: LocalDateTime): Flow<Person>
    fun findAllPersonByCreatedDateBefore(creationDate: LocalDateTime): Flow<Person>
    fun findAllPersonByTermsVersion(termsVersion: LocalDate): Flow<Person>
    fun findAllPersonByTermsVersionBefore(termsVersion: LocalDate): Flow<Person>
    fun findAllPersonByTermsVersionAfter(termsVersion: LocalDate): Flow<Person>
    fun findAllPersonByFraudsterAndCountryCode(isFraudster: Boolean, country: String): Flow<Person>
    suspend fun countByBirthYear(birthYear: Year): Long
    suspend fun countAllPersonByState(state: Person.PersonState): Long
    suspend fun countAllPersonByTermsVersion(termsVersion: LocalDate): Long
    suspend fun countAllPersonByFraudster(isFraudster: Boolean): Long
    suspend fun countAllPerson(): Long
    suspend fun countAllPersonByCountry(countryCode: String): Long
    suspend fun savePerson(person: Person): Either<PersonRepositoryIOError, Person>
    suspend fun updatePerson(person: Person): Either<PersonNotExistPersonRepositoryError, Person>
    suspend fun deletePerson(id: UUID): Either<PersonNotExistPersonRepositoryError, Person>
    suspend fun flagPerson(id: UUID): Either<PersonNotExistPersonRepositoryError, Person>
    fun publishSavePerson(id: UUID)
    fun publishUpdatePerson(id: UUID)
    fun publishDeletePerson(id: UUID)
    fun publishFlagPerson(id: UUID)

    object PersonRepositoryIOError
    object PersonNotExistPersonRepositoryError

}