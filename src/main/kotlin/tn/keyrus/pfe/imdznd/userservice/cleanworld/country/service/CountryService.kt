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
    countryRepository.findAllCountry().map {
        PersonsByCountry(it.code, personService.getAllPersonByCountry(it.code).count())
    }
    fun getAllPersonsInAllCountry() =
        countryRepository.findAllCountry().map {
            PersonInCountry(it.code, personService.getAllPersonByCountry(it.code).toList())
        }

    fun getAllIsFraudstersByCountry(fraud: Boolean) =
        countryRepository.findAllCountry().map {
            PersonInCountry(it.code, personService.getAllPersonByFraudsterAndCountryCode(fraud,it.code).toList())
        }

}

