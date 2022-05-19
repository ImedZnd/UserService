package tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonInCountry
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonsByCountry
import java.util.*

class CountryService(
    private val countryRepository: CountryRepository,
    private val personService: PersonService
) {
    fun getAllCountries() =
        countryRepository.findAllCountry()

    suspend fun getCountryByCode(code: String): Optional<Country> =
        countryRepository.findCountryByCode(code)

    fun getAllCountryByNumberOfPersons() =
        countryRepository.findAllCountry()
            .map { it.code }
            .map(::countryCodeToCountPersonByCountry)

    fun getAllPersonsInAllCountry() =
        countryRepository.findAllCountry()
            .map { it.code }
            .map(::countryCodeToPersonByCountry)

    fun getAllIsFraudstersByCountryByBoolean(fraud: Boolean) =
        countryRepository.findAllCountry()
            .map { it.code }
            .map { countryCodeToAFraudByCountry(it, fraud) }

    private suspend fun countryCodeToAFraudByCountry(countryCode: String, fraudOrNot: Boolean) =
        PersonInCountry(
            countryCode,
            personService.getAllPersonByFraudsterAndCountryCode(fraudOrNot, countryCode).toList()
        )

    private suspend fun countryCodeToPersonByCountry(countryCode: String) =
        PersonInCountry(
            countryCode,
            personService.getAllPersonByCountry(countryCode).toList()
        )

    private suspend fun countryCodeToCountPersonByCountry(countryCode: String) =
        PersonsByCountry(
            countryCode,
            personService.getAllPersonByCountry(countryCode).count()
        )

}

