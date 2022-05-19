package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO.Companion.toDAO
import java.util.*

class CountryDatabaseRepository(
    private val countryReactiveRepository: CountryReactiveRepository
) : CountryRepository {
    override fun findAllCountry() =
        countryReactiveRepository.findAll()
            .asFlow()
            .map(CountryDAO::toCountry)
            .filter { it.isRight }
            .map { it.get() }

    override suspend fun findCountryByCode(code: String) =
        mapCountryOnEitherReturnCountryOrIOError { it.findById(code) }

    override suspend fun saveCountry(country: Country) =
        mapCountryOnEitherReturnCountryOrIOError { it.save(country.toDAO()) }

    private suspend fun mapCountryOnEitherReturnCountryOrIOError(action: (CountryReactiveRepository) -> Mono<CountryDAO>) =
        try {
            action(countryReactiveRepository)
                .map(CountryDAO::toCountry)
                .filter { it.isRight }
                .map { it.get() }
                .map { Optional.of(it) }
                .awaitSingleOrNull()
                ?: Optional.empty()
        } catch (exception: Exception) {
            Optional.empty()
        }
}