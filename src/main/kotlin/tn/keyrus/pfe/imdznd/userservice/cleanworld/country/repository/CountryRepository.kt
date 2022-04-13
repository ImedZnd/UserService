package tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository

import kotlinx.coroutines.flow.Flow
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country

interface CountryRepository {
    fun findAllCountry(): Flow<Country>
    fun findAllCountryByCode(): Flow<Country>
    fun findAllCountryByNumberOfPersons(): Flow<Country>
    fun findAllCountryByNumberOfFraudster(): Flow<Country>
}