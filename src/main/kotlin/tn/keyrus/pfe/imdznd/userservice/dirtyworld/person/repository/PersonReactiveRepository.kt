package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.UsersByCountryDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

@Repository
interface PersonReactiveRepository : ReactiveCrudRepository<PersonDAO, Long> {
    fun findAllByBirthYear(birthYear: Year): Flux<PersonDAO>
    fun findAllByBirthYearAndCountry(birthYear: Year, country: Country): Flux<PersonDAO>
    fun findAllByBirthYearAndHasEmail(birthYear: Year, hasEmail: Boolean): Flux<PersonDAO>
    fun findAllByBirthYearBefore(birthYear: Year): Flux<PersonDAO>
    fun findAllByBirthYearAfter(birthYear: Year): Flux<PersonDAO>
    fun findAllByBirthYearBetween(startYear: Year, endYear: Year): Flux<PersonDAO>
    fun findAllByCountry(country: Country): Flux<PersonDAO>
    fun findAllByCreatedDateAfter(creationDate: LocalDateTime): Flux<PersonDAO>
    fun findAllByCreatedDateBefore(creationDate: LocalDateTime): Flux<PersonDAO>
    fun findAllByCreatedDateBetween(creationDateStart: LocalDateTime, creationDateEnd: LocalDateTime): Flux<PersonDAO>
    fun findAllByState(state: Person.PersonState): Flux<PersonDAO>
    fun findAllByTermsVersion(termsVersion: LocalDate): Flux<PersonDAO>
    fun findAllByTermsVersionBefore(termsVersion: LocalDate): Flux<PersonDAO>
    fun findAllByTermsVersionAfter(termsVersion: LocalDate): Flux<PersonDAO>
    fun findAllByTermsVersionBetween(termsVersionStart: LocalDate, termsVersionEnd: LocalDate): Flux<PersonDAO>
    fun findAllByFailedSignInAttemptsGreaterThan(failedSignInAttempts: Int): Flux<PersonDAO>
    fun findAllByFailedSignInAttemptsLessThan(failedSignInAttempts: Int): Flux<PersonDAO>
    fun findAllByKyc(kyc: Person.PersonKYC): Flux<PersonDAO>
    fun findAllByHasEmail(hasEmail: Boolean): Flux<PersonDAO>
    fun findAllByNumberOfFlags(numberOfFlags: Int): Flux<PersonDAO>
    fun findAllByNumberOfFlagsGreaterThan(numberOfFlags: Int): Flux<PersonDAO>
    fun findAllByNumberOfFlagsLessThan(numberOfFlags: Int): Flux<PersonDAO>
    fun findAllByFraudster(isFraudster: Boolean): Flux<PersonDAO>
    fun findAllByFraudsterAndCountry(isFraudster: Boolean, country: Country): Flux<PersonDAO>
    fun findAllByPhoneCountryContaining(phoneCountry: String): Flux<PersonDAO>
    fun countByBirthYear(birthYear: Year): Mono<Long>
    fun countByCountry(country: Country): Mono<Long>
    fun countByState(state: Person.PersonState): Mono<Long>
    fun countByCreatedDate(creationDate: LocalDateTime): Mono<Long>
    fun countByTermsVersion(termsVersion: LocalDate): Mono<Long>
    fun countByFraudster(isFraudster: Boolean): Mono<Long>

    @Query(
        "select p.country count(p.id)" +
                "from person p" +
                "group by p.country"
    )
    fun countByFraudsterAndCountry(isFraudster: Boolean): Mono<UsersByCountryDAO>
}