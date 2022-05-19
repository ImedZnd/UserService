package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.rest.handler

import kotlinx.coroutines.flow.map
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.web.reactive.function.server.*
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service.CountryService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dto.CountryDTO.Companion.toCountryDTO
import java.util.*

class CountryHandler(
    private val countryService: CountryService,
    private val messageSource: MessageSource
) {
    suspend fun getAllCountries() =
        ServerResponse.ok()
            .bodyAndAwait(
                countryService.getAllCountries()
                    .map { it.toCountryDTO() }
            )

    suspend fun getCountryByCode(request: ServerRequest) =
        countryService.getCountryByCode(request.pathVariable("code"))
            .let {
                if (it.isPresent)
                    ServerResponse
                        .ok()
                        .bodyValueAndAwait(
                            it.get().toCountryDTO()
                        )
                else ServerResponse
                    .badRequest()
                    .header("error", headerErrorInBadRequestError())
                    .buildAndAwait()
            }

    suspend fun getAllCountryByNumberOfPersons() =
        ServerResponse
            .ok()
            .bodyAndAwait(
                countryService.getAllCountryByNumberOfPersons()
            )

    suspend fun getAllIsFraudstersByCountry(request: ServerRequest) =
        ServerResponse.ok()
            .bodyAndAwait(
                countryService.getAllIsFraudstersByCountryByBoolean(
                    request.pathVariable("isFraud").toBoolean()
                )
            )

    suspend fun getAllPersonsInAllCountry() =
        ServerResponse.ok()
            .bodyAndAwait(
                countryService.getAllPersonsInAllCountry()
            )

    private fun headerErrorInBadRequestError() =
        try {
            messageSource.getMessage("CountryCodeError", null, Locale.US)
        } catch (exception: NoSuchMessageException) {
            "No Such Message Exception Raised"
        }

}