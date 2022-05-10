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
                countryService
                    .getAllCountries()
                    .map {
                        it.toCountryDTO()
                    }
            )

    suspend fun getCountryByCode(request: ServerRequest): ServerResponse {
        val countryCode =
            countryService
                .getCountryByCode(
                    request.pathVariable("code")
                )
        return if (countryCode.isPresent)
            ServerResponse
                .ok()
                .bodyValueAndAwait(
                    countryCode.get()
                )
        else ServerResponse
            .badRequest()
            .header("error", headerErrorInBadRequestError("CountryCodeError"))
            .buildAndAwait()

    }

    suspend fun getAllCountryByNumberOfPersons() =
        ServerResponse
            .ok()
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

    private fun headerErrorInBadRequestError(string: String) =
        try {
            messageSource.getMessage(string, null, Locale.US)
        } catch (exception: NoSuchMessageException) {
            "No Such Message Exception Raised"
        }

    object CountryWithCodeNotFound
}