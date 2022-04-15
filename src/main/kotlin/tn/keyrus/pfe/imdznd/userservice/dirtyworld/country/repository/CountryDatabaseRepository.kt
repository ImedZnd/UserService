package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import reactor.core.publisher.Mono
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model.Country
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO.Builder.toDAO

class CountryDatabaseRepository(
    private val countryReactiveRepository: CountryReactiveRepository
) : CountryRepository {
    override fun findAllCountry() =
        countryReactiveRepository.findAll()
            .asFlow()
            .map { it.toCountry() }
            .filter { it.isRight }
            .map { it.get() }

    override suspend fun findCountryByCode(code: String): Either<CountryRepository.CountryRepositoryIOError, Country> =
            mapCountryOnEitherReturnCountryOrIOError{ it.findByCode(code) }

    override fun findAllCountryByNumberOfPersons() =
        countryReactiveRepository
            .findAllCountryByNumberOfPersons()

    override fun findAllIsFraudstersByCountry(fraud: Boolean) =
        countryReactiveRepository.findAllIsFraudstersByCountry(fraud)

    override suspend fun saveCountry(country: Country): Either<CountryRepository.CountryRepositoryIOError, Country> =
        mapCountryOnEitherReturnCountryOrIOError{ it.save(country.toDAO()) }

    private suspend fun mapCountryOnEitherReturnCountryOrIOError(action: (CountryReactiveRepository) -> Mono<CountryDAO>): Either<CountryRepository.CountryRepositoryIOError, Country> =
        try {
           action(countryReactiveRepository)
               .map { it.toCountry() }
               .filter { it.isRight }
               .doOnEach { print(it) }
               .map { it.get() }
               .map { Either.right<CountryRepository.CountryRepositoryIOError, Country>(it) }
               .awaitSingleOrNull()
               ?: Either.left(CountryRepository.CountryRepositoryIOError)
       }catch (exception: Exception){
           Either.left(CountryRepository.CountryRepositoryIOError)
       }
}