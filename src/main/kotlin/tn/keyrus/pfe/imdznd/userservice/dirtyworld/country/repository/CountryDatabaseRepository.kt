package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO.Builder.toDAO
import java.util.Optional

class CountryDatabaseRepository(
    private val countryReactiveRepository: CountryReactiveRepository
) : CountryRepository {
    override fun findAllCountry() =
        countryReactiveRepository.findAll()
            .asFlow()
            .map { it.toCountry() }
            .filter { it.isRight }
            .map { it.get() }

    override suspend fun findCountryByCode(code: String): Optional<Country> =
            mapCountryOnEitherReturnCountryOrIOError{ it.findByCode(code) }

    override suspend fun saveCountry(country: Country): Optional<Country> =
        mapCountryOnEitherReturnCountryOrIOError{ it.save(country.toDAO()) }

    private suspend fun mapCountryOnEitherReturnCountryOrIOError(action: (CountryReactiveRepository) -> Mono<CountryDAO>): Optional<Country> =
        try {
           action(countryReactiveRepository)
               .map { it.toCountry() }
               .filter { it.isRight }
               .map { it.get() }
               .map { Optional.of(it) }
               .awaitSingleOrNull()
               ?: Optional.empty()
       }catch (exception: Exception){
            Optional.empty()
       }
}