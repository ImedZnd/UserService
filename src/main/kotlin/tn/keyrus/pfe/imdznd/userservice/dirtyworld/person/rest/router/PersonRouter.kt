package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.annotation.PersonRouterInfo
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.handler.PersonHandler

@Configuration
class PersonRouter {

    @Bean
    @PersonRouterInfo
    fun personRoutes(personHandler: PersonHandler) = coRouter {
        "/person".nest {
            GET("/all") { personHandler.getAllPersons() }
            GET("/birthYear/{year}", personHandler::getAllPersonsByBirthYear)
            GET("/birthYearBefore/{year}", personHandler::getAllPersonsByBirthYearBefore)
            GET("/birthYearAfter/{year}", personHandler::getAllPersonsByBirthYearAfter)
            POST("/birthYearBetween", personHandler::getAllPersonsByBirthYearBetween)
            GET("/state/{state}", personHandler::getAllPersonsByState)
            GET("/country/{country}", personHandler::getAllPersonByCountry)
            POST("/createdDateRange", personHandler::getAllPersonByCreatedDateInRange)
            POST("/termVersionInRange", personHandler::getAllPersonByTermsVersionBetween)
            POST("/termVersionBefore", personHandler::getAllPersonByTermVersionBefore)
            POST("/termVersionAfter", personHandler::getAllPersonByTermVersionAfter)
            GET("/phoneCountry/{phoneCountry}", personHandler::getAllPersonByPhoneCountry)
            GET("/KYC/{KYC}", personHandler::getAllPersonByKYC)
            GET("/hasEmail/{hasEmail}", personHandler::getAllPersonByHasEmail)
            GET("/isFraud/{isFraud}", personHandler::getAllPersonByIsFraud)
            GET("/numberOfFlags/{numberOfFlags}", personHandler::getAllPersonsNumberOfFlags)
            GET("/numberOfFlagsLessThan/{numberOfFlags}", personHandler::getAllPersonsNumberOfFlagsLessThan)
            GET("/numberOfFlagsGreaterThan/{numberOfFlags}", personHandler::getAllPersonsNumberOfFlagsGreaterThan)
            POST("/createdDateBefore", personHandler::getAllPersonByCreatedDateBefore)
            POST("/createdDateAfter", personHandler::getAllPersonByCreatedDateAfter)
            POST("/fraudAndCountryCode", personHandler::getAllPersonByFraudsterAndCountryCode)
            GET("/countAllPerson") { personHandler.countAllUsers() }
            GET("/countAllPersonByState/{state}", personHandler::countAllUsersByState)
            GET("/countAllPersonByBirthYear/{year}", personHandler::countAllPersonByBirthYear)
            POST("/countAllUsersByTermsVersion", personHandler::countAllUsersByTermsVersion)
            GET("/countAllUsersByIsFraud/{isFraud}", personHandler::countAllUsersByIsFraud)
            GET("/countAllUsersByCountry/{country}", personHandler::countAllUsersByCountry)
            POST("/termsVersion", personHandler::getAllPersonByTermsVersion)
            POST("/save", personHandler::savePerson)
            POST("/update", personHandler::updatePerson)
            GET("/delete/{personId}", personHandler::deletePerson)
            GET("/flag/{personId}", personHandler::flagPerson)
            GET("/id/{personId}", personHandler::getPersonById)
            GET("/fraud/{personId}", personHandler::fraudPerson)
            GET("/unfraud/{personId}", personHandler::unFraudPerson)
        }
    }
}