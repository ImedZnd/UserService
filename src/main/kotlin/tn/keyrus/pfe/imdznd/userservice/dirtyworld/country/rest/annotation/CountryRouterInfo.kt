package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.annotation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dto.CountryDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.handler.CountryHandler
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonInCountry
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao.PersonsByCountry

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@RouterOperations(
    RouterOperation(
        path = "/country/all",
        method = [RequestMethod.GET],
        beanClass = CountryHandler::class,
        beanMethod = "getAllCountries",
        operation = Operation(
            operationId = "getAllCountries",
            method = "GET",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all country successfully.",
                    content = [Content(schema = Schema(implementation = CountryDTO::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/country/allPersonInAllCountry",
        method = [RequestMethod.GET],
        beanClass = CountryHandler::class,
        beanMethod = "getAllPersonsInAllCountry",
        operation = Operation(
            operationId = "getAllPersonsInAllCountry",
            method = "GET",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all person in all country successfully.",
                    content = [Content(schema = Schema(implementation = PersonInCountry::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/country/personPerCountry",
        method = [RequestMethod.GET],
        beanClass = CountryHandler::class,
        beanMethod = "getAllCountryByNumberOfPersons",
        operation = Operation(
            operationId = "getAllCountryByNumberOfPersons",
            method = "GET",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all country successfully.",
                    content = [Content(schema = Schema(implementation = CountryDTO::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/country/code/{code}",
        method = [RequestMethod.GET],
        beanClass = CountryHandler::class,
        beanMethod = "getCountryByCode",
        operation = Operation(
            operationId = "getCountryByCode",
            method = "GET",
            parameters = [Parameter(
                name = "code",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get country by  ode successfully.",
                    content = [Content(schema = Schema(implementation = CountryDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Country Code.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ]
        )
    ),

    RouterOperation(
        path = "/country/fraudPerCountry/{code}",
        method = [RequestMethod.GET],
        beanClass = CountryHandler::class,
        beanMethod = "getAllIsFraudstersByCountry",
        operation = Operation(
            operationId = "getAllIsFraudstersByCountry",
            method = "GET",
            parameters = [Parameter(
                name = "code",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Is Fraudsters By Country successfully.",
                    content = [Content(schema = Schema(implementation = PersonsByCountry::class))]
                ),
            ]
        )
    ),
)

annotation class CountryRouterInfo
