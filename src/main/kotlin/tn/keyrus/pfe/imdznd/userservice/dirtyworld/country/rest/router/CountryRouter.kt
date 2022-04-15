package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.handler.CountryHandler

@Configuration
class CountryRouter {

    @Bean
    fun routes(countryHandler: CountryHandler) = coRouter {
        "/country".nest {
            GET("/all") { countryHandler.getAllCountries() }
            GET("/code/{code}") { countryHandler.getCountryByCode(it) }
            GET("/personPerCountry") { countryHandler.getAllCountryByNumberOfPersons() }
            GET("/fraudPerCountry") { countryHandler.getAllIsFraudstersByCountry(it) }
        }
    }
}