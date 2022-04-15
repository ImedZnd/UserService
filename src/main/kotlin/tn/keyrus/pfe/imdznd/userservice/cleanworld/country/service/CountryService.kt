package tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service

import io.vavr.control.Either
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository

class CountryService(
    private val countryDatabaseRepository: CountryRepository
) {
    fun getAllCountries() =
        countryDatabaseRepository.findAllCountry()

    suspend fun getCountryByCode(code: String): Either<CountryWithCodeNotFound, Country> =
        countryDatabaseRepository.findCountryByCode(code)
            .mapLeft { CountryWithCodeNotFound }

    fun getAllCountryByNumberOfPersons() =
        countryDatabaseRepository.findAllCountryByNumberOfPersons()

    fun getAllIsFraudstersByCountry(fraud: Boolean) =
        countryDatabaseRepository.findAllIsFraudstersByCountry(fraud)

    object CountryWithCodeNotFound
}

