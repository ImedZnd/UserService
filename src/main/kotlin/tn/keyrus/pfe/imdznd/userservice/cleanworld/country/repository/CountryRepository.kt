package tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository

import kotlinx.coroutines.flow.Flow
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonsByCountry
import java.util.*

interface CountryRepository {
    fun findAllCountry(): Flow<Country>
    suspend fun findCountryByCode(code : String): Optional<Country>
    fun findAllCountryByNumberOfPersons(): Flow<PersonsByCountry>
    fun findAllIsFraudstersByCountry(fraud: Boolean):Flow<PersonsByCountry>
    suspend fun saveCountry(country: Country): Optional<Country>
}