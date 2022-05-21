package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.annotation.CountryRouterInfo
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.handler.CountryHandler

@Configuration
class CountryRouter {

    @Bean
    @CountryRouterInfo
    fun countryRoutes(countryHandler: CountryHandler) = coRouter {
        "/country".nest {
            GET("/all") { countryHandler.getAllCountries() }
            GET("/personPerCountry") { countryHandler.getAllCountryByNumberOfPersons() }
            GET("/code/{code}", countryHandler::getCountryByCode)
            GET("/fraudPerCountry/{isFraud}", countryHandler::getAllIsFraudstersByCountry)
            GET("/allPersonInAllCountry") { countryHandler.getAllPersonsInAllCountry() }
        }
    }
}