package tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository

import kotlinx.coroutines.flow.Flow
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import java.time.LocalDate
import java.time.Year

interface PersonRepository {

    fun findAllPerson(): Flow<Person>
    fun findAllPersonByBirthYear(year: Year): Flow<Person>
    fun findAllPersonByState(state: Person.PersonState): Flow<Person>
    fun findAllPersonByCountry(countryCode: String): Flow<Person>
    fun findAllPersonByCreatedDateInRange(startDate: LocalDate, endDate: LocalDate = startDate): Flow<Person>
    fun findAllPersonByTermsVersion(startDate: LocalDate, endDate: LocalDate = startDate): Flow<Person>
    fun findAllPersonByPhoneCountry(phoneCountry: String): Flow<Person>
    fun findAllPersonByKYC(kyc: Person.PersonKYC): Flow<Person>
    fun findAllPersonByHasEmail(hasEmail: Boolean): Flow<Person>
    fun findAllPersonByNumberOfFlags(numberOfFlags: Int): Flow<Person>
    fun findAllPersonGroupByCountry(countryCode: String): Flow<Person>
    fun findAllPersonByIsFraudster(isFraudster: Boolean): Flow<Person>
    fun findAllPersonByIsFraudsterGroupByCountry(isFraudster: Boolean,countryCode: String): Flow<Person>

    fun countAllPerson(): Flow<Person>
    fun countAllPersonByFraudsters(): Flow<Person>
    fun countAllPersonByCountry(): Flow<Person>

}