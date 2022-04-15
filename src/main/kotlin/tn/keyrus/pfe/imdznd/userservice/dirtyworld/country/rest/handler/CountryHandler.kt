package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.handler

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service.CountryService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dto.CountryDTO

class CountryHandler(
    private val countryService: CountryService
) {
    suspend fun getAllCountries() =
        ServerResponse.ok()
            .bodyAndAwait(
                countryService
                    .getAllCountries()
                    .map {
                        CountryDTO.toCountryDTO(it)
                    }
            )

    suspend fun getCountryByCode(request: ServerRequest): ServerResponse {
        val countryCode =
            countryService
                .getCountryByCode(
                    request.pathVariable("code")
                )
        return if (countryCode.isRight)
            ServerResponse
                .ok()
                .bodyAndAwait(
                    flowOf(countryCode.get())
                )
        else ServerResponse
            .badRequest()
            .bodyAndAwait(
                flowOf(CountryWithCodeNotFound)
            )
    }

    suspend fun getAllCountryByNumberOfPersons() =
        ServerResponse.ok()
            .bodyAndAwait(
                countryService
                    .getAllCountryByNumberOfPersons()
            )

    suspend fun getAllIsFraudstersByCountry(request: ServerRequest) =
        ServerResponse.ok()
            .bodyAndAwait(
                countryService
                    .getAllIsFraudstersByCountry(
                        request.pathVariable("code").toBoolean()
                    )
            )

    object CountryWithCodeNotFound
}