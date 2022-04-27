package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.handler.PersonHandler

@Configuration
class PersonRouter {

    @Bean
    fun personRoutes(personHandler: PersonHandler) = coRouter {
        "/person".nest {
            GET("/all") { personHandler.getAllPersons() }
            POST("/birthYear") { personHandler.getAllPersonsByBirthYear(it) }
            POST("/birthYearBefore") { personHandler.getAllPersonsByBirthYearBefore(it) }
            POST("/birthYearAfter") { personHandler.getAllPersonsByBirthYearAfter(it) }
            POST("/birthYearBetween") { personHandler.getAllPersonsByBirthYearBetween(it) }
            GET("/state/{state}") { personHandler.getAllPersonsByState(it) }
            GET("/country/{country}") { personHandler.getAllPersonByCountry(it) }
            POST("/createdDateRange") { personHandler.getAllPersonByCreatedDateInRange(it) }
            POST("/termVersionInRange") { personHandler.getAllPersonByTermsVersionBetween(it) }
            POST("/termVersionBefore") { personHandler.getAllPersonByTermVersionBefore(it) }
            POST("/termVersionAfter") { personHandler.getAllPersonByTermVersionAfter(it) }
            GET("/phoneCountry/{phoneCountry}") { personHandler.getAllPersonByPhoneCountry(it) }
            GET("/KYC/{KYC}") { personHandler.getAllPersonByKYC(it) }
            GET("/hasEmail/{hasEmail}") { personHandler.getAllPersonByHasEmail(it) }
            GET("/isFraud/{isFraud}") { personHandler.getAllPersonByIsFraud(it) }
            GET("/numberOfFlags/{numberOfFlags}") { personHandler.getAllPersonsNumberOfFlags(it) }
            GET("/numberOfFlagsLessThan/{numberOfFlags}") { personHandler.getAllPersonsNumberOfFlagsLessThan(it) }
            GET("/numberOfFlagsGreaterThan/{numberOfFlags}") { personHandler.getAllPersonsNumberOfFlagsGreaterThan(it) }
            POST("/createdDateBefore") { personHandler.getAllPersonByCreatedDateBefore(it) }
            POST("/createdDateAfter") { personHandler.getAllPersonByCreatedDateAfter(it) }
            POST("/fraudAndCountryCode") { personHandler.getAllPersonByFraudsterAndCountryCode(it) }
            GET("/countAllPerson") { personHandler.countAllUsers() }
            GET("/countAllPersonByState/{state}") { personHandler.countAllUsersByState(it) }
            POST("/countAllUsersByTermsVersion") { personHandler.countAllUsersByTermsVersion(it) }
            GET("/countAllUsersByIsFraud/{isFraud}") { personHandler.countAllUsersByIsFraud(it) }
            GET("/countAllUsersByCountry/{country}") { personHandler.countAllUsersByCountry(it) }
            POST("/save") { personHandler.savePerson(it) }
            POST("/update") { personHandler.updatePerson(it) }
            POST("/delete") { personHandler.deletePerson(it) }
            POST("/flag") { personHandler.flagPerson(it) }
            POST("/id") { personHandler.getPersonById(it) }
        }
    }
}