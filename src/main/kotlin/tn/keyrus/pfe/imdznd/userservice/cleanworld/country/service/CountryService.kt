package tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service

import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import java.util.Optional

class CountryService(
    private val countryDatabaseRepository: CountryRepository
) {
    fun getAllCountries() =
        countryDatabaseRepository.findAllCountry()

    suspend fun getCountryByCode(code: String):Optional<Country> =
        countryDatabaseRepository.findCountryByCode(code)

    fun getAllCountryByNumberOfPersons() =
        countryDatabaseRepository.findAllCountryByNumberOfPersons()

    fun getAllIsFraudstersByCountry(fraud: Boolean) =
        countryDatabaseRepository.findAllIsFraudstersByCountry(fraud)

}

