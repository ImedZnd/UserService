package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonDAO
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
interface PersonReactiveRepository : ReactiveCrudRepository<PersonDAO, Long> {
    fun findAllByBirthYear(birthYear: Int): Flux<PersonDAO>
    fun findAllByBirthYearAndCountryCode(birthYear: Int, countryCode: String): Flux<PersonDAO>
    fun findAllByBirthYearAndHasEmail(birthYear: Int, hasEmail: Boolean): Flux<PersonDAO>
    fun findAllByBirthYearBefore(birthYear: Int): Flux<PersonDAO>
    fun findAllByBirthYearAfter(birthYear: Int): Flux<PersonDAO>
    fun findAllByBirthYearBetween(birthYear: Int, birthYear2: Int): Flux<PersonDAO>

    @Query(
        """select * 
                from person p
                where country == :country"""
    )
    fun findAllByCountry(country: String): Flux<PersonDAO>
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
    fun findAllByFraudsterAndCountryCode(isFraudster: Boolean, country: String): Flux<PersonDAO>
    fun findAllByPhoneCountryContaining(phoneCountry: String): Flux<PersonDAO>
    fun countByBirthYear(birthYear: Int): Mono<Long>
    fun countByCountryCode(country: String): Mono<Long>
    fun countByState(state: Person.PersonState): Mono<Long>
    fun countByCreatedDate(creationDate: LocalDateTime): Mono<Long>
    fun countByTermsVersion(termsVersion: LocalDate): Mono<Long>
    fun countByFraudster(isFraudster: Boolean): Mono<Long>

}