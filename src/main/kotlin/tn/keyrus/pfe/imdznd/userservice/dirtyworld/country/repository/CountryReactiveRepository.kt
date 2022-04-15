package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonsByCountry

@Repository
interface CountryReactiveRepository : ReactiveCrudRepository<CountryDAO, Long> {
    fun findByCode(code : String): Mono<CountryDAO>
    @Query(
        """select p.country_code AS country,count(p.id) AS numberOfUsers
                from person p , country c
                where c.code = p.country_code
                and fraudster = :1
                group by p.country_code"""
    )
    fun findAllIsFraudstersByCountry(fraudster: Boolean): Flow<PersonsByCountry>

    @Query(
        """select p.country_code AS country,count(p.id) AS numberOfUsers
                from person p
                group by p.country_code"""
    )
    fun findAllCountryByNumberOfPersons(): Flow<PersonsByCountry>

}