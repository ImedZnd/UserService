package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.repository.CountryRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service.CountryService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository.CountryDatabaseRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository.CountryReactiveRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.handler.CountryHandler

@Configuration
class CountryConfiguration {

    @Bean
    fun countryDatabaseRepository(
        reactiveDatabaseRepository: CountryReactiveRepository
    ): CountryRepository =
        CountryDatabaseRepository(reactiveDatabaseRepository)

    @Bean
    fun countryService(
        countryDatabaseRepository: CountryRepository
    ): CountryService =
        CountryService(countryDatabaseRepository)

    @Bean
    fun countryHandler(
        countryService: CountryService
    ): CountryHandler =
        CountryHandler(countryService)

}