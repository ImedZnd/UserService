package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.handler

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.context.MessageSource
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service.CountryService
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.*
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO.Builder.toPersonDTO
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.util.*

class PersonHandler(
    private val personService: PersonService,
    private val countryService: CountryService,
    private val messageSource: MessageSource
) {
    suspend fun getAllPersons() =
        okServerResponse(
            personService.getAllPersons()
        )

    suspend fun getPersonById(serverRequest: ServerRequest): ServerResponse {
        serverRequest.awaitBody<PersonIdDTO>().id.let { id ->
            val foundPerson = personService.getPersonByID(id).awaitSingleOrNull()
                ?: return notFoundServerResponse("PersonNotExist")
            return okServerResponse(flowOf(foundPerson.get()))
        }
    }

        suspend fun getAllPersonByCountry(serverRequest: ServerRequest) =
            if (countryService.getCountryByCode(
                    serverRequest
                        .pathVariable("country")
                        .uppercase()
                )
                    .isPresent
            )
                okServerResponse(
                    personService
                        .getAllPersonByCountry(
                            serverRequest
                                .pathVariable("country")
                                .uppercase()
                        )
                )
            else badRequestServerResponse("CountryCodeError")

        suspend fun getAllPersonsByBirthYearBetween(serverRequest: ServerRequest) =
            serverRequest
                .awaitBody<YearRangeDTO>()
                .let { yearRange ->
                    val yearRangeCheck = yearRange.checkStartYearAndEndYear()
                    if (yearRangeCheck.isPresent)
                        badRequestServerResponse("YearRangeError")
                    else okServerResponse(
                        personService.getAllPersonByBirthYearBetween(
                            yearRange.startYear.toYear().get(),
                            yearRange.endYear.toYear().get()
                        )
                    )
                }

        suspend fun getAllPersonsByBirthYearBefore(serverRequest: ServerRequest) =
            yearOrErrorOfEitherBirthYearBefore(extractYearFromServerRequest(serverRequest))

        suspend fun getAllPersonsByBirthYearAfter(serverRequest: ServerRequest) =
            yearOrErrorOfEitherBirthYearAfter(extractYearFromServerRequest(serverRequest))

        private suspend fun yearOrErrorOfEitherBirthYearBefore(either: Either<YearDTO.YearConversionError, Year>): ServerResponse =
            if (either.isLeft)
                badRequestServerResponse("YearConversionError")
            else okServerResponse(
                personService.getAllPersonByBirthYearBefore(either.get())
            )

        private suspend fun yearOrErrorOfEitherBirthYearAfter(either: Either<YearDTO.YearConversionError, Year>): ServerResponse =
            if (either.isLeft)
                badRequestServerResponse("YearConversionError")
            else okServerResponse(
                personService.getAllPersonByBirthYearAfter(either.get())
            )

        private suspend fun extractYearFromServerRequest(serverRequest: ServerRequest) =
            serverRequest
                .awaitBody<YearDTO>()
                .also(::println)
                .toYear()

        suspend fun getAllPersonsByBirthYear(serverRequest: ServerRequest) =
            yearOrErrorOfEitherBirthYear(extractYearFromServerRequest(serverRequest))

        private suspend fun yearOrErrorOfEitherBirthYear(either: Either<YearDTO.YearConversionError, Year>): ServerResponse =
            if (either.isLeft)
                badRequestServerResponse("YearConversionError")
            else okServerResponse(
                personService.getAllPersonsByBirthYear(either.get())
            )

        suspend fun getAllPersonsByState(serverRequest: ServerRequest) =
            if (Person.PersonState.values().map { it.name }.contains(
                    serverRequest.pathVariable("state").uppercase()
                ).not()
            )
                badRequestServerResponse("statePersonProblem")
            else okServerResponse(
                personService.getAllPersonsByState(
                    Person.PersonState.valueOf(
                        serverRequest.pathVariable("state").uppercase()
                    )
                )
            )

        suspend fun getAllPersonByKYC(serverRequest: ServerRequest) =
            if (Person.PersonKYC.values().map { it.name }.contains(
                    serverRequest.pathVariable("KYC").uppercase()
                ).not()
            )
                badRequestServerResponse("KYCProblem")
            else okServerResponse(
                personService.getAllPersonByKYC(
                    Person.PersonKYC.valueOf(
                        serverRequest.pathVariable("KYC").uppercase()
                    )
                )
            )

        suspend fun getAllPersonsNumberOfFlags(serverRequest: ServerRequest): ServerResponse {
            val checkingInteger = checkInt(serverRequest, "numberOfFlags")
            return if (checkingInteger.isEmpty)
                badRequestServerResponse("errorInNumberOfFlags")
            else okServerResponse(personService.getAllPersonByNumberOfFlags(checkingInteger.get()))
        }


        suspend fun getAllPersonsNumberOfFlagsLessThan(serverRequest: ServerRequest): ServerResponse {
            val checkingInteger = checkInt(serverRequest, "numberOfFlags")
            return if (checkingInteger.isEmpty)
                badRequestServerResponse("errorInNumberOfFlags")
            else okServerResponse(personService.getAllPersonByNumberOfFlagsLessThan(checkingInteger.get()))
        }


        suspend fun getAllPersonsNumberOfFlagsGreaterThan(serverRequest: ServerRequest): ServerResponse {
            val checkingInteger = checkInt(serverRequest, "numberOfFlags")
            return if (checkingInteger.isEmpty)
                badRequestServerResponse("errorInNumberOfFlags")
            else okServerResponse(personService.getAllPersonByNumberOfFlagsGreaterThan(checkingInteger.get()))
        }


        private fun checkInt(serverRequest: ServerRequest, numberFlags: String): Optional<Int> {
            return try {
                val numberOfFlags = serverRequest.pathVariable(numberFlags).toInt()
                if (numberOfFlags < 0)
                    return Optional.empty()
                Optional.of(numberOfFlags)
            } catch (exception: NumberFormatException) {
                Optional.empty()
            }
        }

        suspend fun getAllPersonByHasEmail(serverRequest: ServerRequest) =
            okServerResponse(
                personService.getAllPersonByHasEmail(
                    serverRequest.pathVariable("hasEmail").toBoolean()
                )
            )

        suspend fun getAllPersonByIsFraud(serverRequest: ServerRequest) =
            okServerResponse(
                personService.getAllPersonByIsFraudster(
                    serverRequest.pathVariable("isFraud").toBoolean()
                )
            )

        suspend fun getAllPersonByPhoneCountry(serverRequest: ServerRequest) =
            if (
                personService
                    .getAllPersonByPhoneCountry(
                        serverRequest
                            .pathVariable("phoneCountry")
                            .uppercase()
                    ).count() == 0
            )
                badRequestServerResponse("PhoneCountryProblem")
            else okServerResponse(
                personService.getAllPersonByPhoneCountry(
                    serverRequest.pathVariable("phoneCountry").uppercase()
                )
            )

        suspend fun getAllPersonByTermsVersionBetween(serverRequest: ServerRequest) =
            serverRequest.awaitBody<DateRangeDTO>()
                .let { dateRange ->
                    val dateRangeCheck = dateRange.checkStartDateAndEndDate()
                    if (dateRangeCheck.isPresent)
                        badRequestServerResponse("DateRangeError")
                    else
                        okServerResponse(
                            personService
                                .getAllPersonByTermsVersionBetween(
                                    dateRange.startDate.toLocalDate().get(),
                                    dateRange.endDate.toLocalDate().get()
                                )
                        )
                }

        suspend fun getAllPersonByCreatedDateInRange(serverRequest: ServerRequest) =
            serverRequest.awaitBody<DateRangeDTO>()
                .let { dateRange ->
                    val dateRangeCheck = dateRange.checkStartDateAndEndDate()
                    print(dateRangeCheck.isPresent)
                    if (dateRangeCheck.isPresent)
                        badRequestServerResponse("DateRangeError")
                    else
                        okServerResponse(
                            personService
                                .getAllPersonByCreatedDateInRange(
                                    dateRange.startDate.toLocalDate().get(),
                                    dateRange.endDate.toLocalDate().get()
                                )
                        )
                }

        suspend fun getAllPersonByCreatedDateAfter(serverRequest: ServerRequest) =
            serverRequest.awaitBody<DateDTO>()
                .let { date ->
                    val dateTime = date.toLocalDate()
                    println(date)
                    if (dateTime.isLeft)
                        badRequestServerResponse("DateError")
                    else
                        okServerResponse(
                            personService
                                .getAllPersonByCreatedDateAfter(LocalDateTime.of(dateTime.get(), LocalTime.now()))
                        )
                }

        suspend fun getAllPersonByCreatedDateBefore(serverRequest: ServerRequest) =
            serverRequest.awaitBody<DateDTO>()
                .let { date ->
                    val dateTime = date.toLocalDate()
                    println(date)
                    if (dateTime.isLeft)
                        badRequestServerResponse("DateError")
                    else
                        okServerResponse(
                            personService
                                .getAllPersonByCreatedDateBefore(LocalDateTime.of(dateTime.get(), LocalTime.now()))
                        )
                }

        suspend fun getAllPersonByTermVersionBefore(serverRequest: ServerRequest) =
            serverRequest.awaitBody<DateDTO>()
                .let { date ->
                    val dateTime = date.toLocalDate()
                    if (dateTime.isLeft)
                        badRequestServerResponse("DateError")
                    else
                        okServerResponse(
                            personService
                                .getAllPersonByTermsVersionBefore(date.toLocalDate().get())
                        )
                }

        suspend fun getAllPersonByTermVersionAfter(serverRequest: ServerRequest) =
            serverRequest.awaitBody<DateDTO>()
                .let { date ->
                    val dateTime = date.toLocalDate()
                    if (dateTime.isLeft)
                        badRequestServerResponse("DateError")
                    else
                        okServerResponse(
                            personService
                                .getAllPersonByTermsVersionAfter(date.toLocalDate().get())
                        )
                }

        suspend fun getAllPersonByFraudsterAndCountryCode(serverRequest: ServerRequest) =
            serverRequest.awaitBody<FraudsterAndCountryCodeDTO>()
                .let { fraudsterAndCountryCodeDTO ->
                    val countryCode = countryService.getCountryByCode(fraudsterAndCountryCodeDTO.countryCode)
                    if (countryCode.isEmpty)
                        badRequestServerResponse("CountryCodeError")
                    else okServerResponse(
                        personService.getAllPersonByFraudsterAndCountryCode(
                            fraudsterAndCountryCodeDTO.isFraud,
                            fraudsterAndCountryCodeDTO.countryCode
                        )
                    )
                }

        suspend fun savePerson(serverRequest: ServerRequest): ServerResponse {
            serverRequest.awaitBody<PersonDTO>()
                .let { person ->
                    val personToSave = person.toPerson()
                    if (personToSave.isRight) {
                        val personSaved = personService.savePerson(personToSave.get())
                        return if (personSaved.isRight)
                            okServerResponse(flowOf(personSaved.get()))
                        else
                            badRequestServerResponse("PersonExist")
                    }
                    return badRequestServerResponse("BadPersonInRequest")
                }
        }

        suspend fun updatePerson(serverRequest: ServerRequest): ServerResponse {
            serverRequest.awaitBody<PersonDTO>()
                .let { person ->
                    val personToUpdate = person.toPerson()
                    if (personToUpdate.isRight) {
                        val personUpdated = personService.updatePerson(personToUpdate.get())
                        return if (personUpdated.isRight) {
                            println(personUpdated.get())
                            okServerResponse(flowOf(personUpdated.get()))
                        } else {
                            println(personUpdated.left)
                            notFoundServerResponse("PersonNotExist")
                        }
                    }
                    return badRequestServerResponse("BadPersonInRequest")
                }
        }

        suspend fun deletePerson(serverRequest: ServerRequest): ServerResponse {
            try {
                val id = serverRequest.awaitBody<PersonIdDTO>().id
                val deletedPerson = personService.deletePerson(id)
                if (deletedPerson.isRight) {
                    return okServerResponse(flowOf(deletedPerson.get()))
                } else return notFoundServerResponse("PersonNotExist")
            } catch (exception: Exception) {
                return badRequestServerResponse("BadPersonInRequest")
            }
        }

        suspend fun flagPerson(serverRequest: ServerRequest): ServerResponse {
             try {
                val id = serverRequest.awaitBody<PersonIdDTO>().id
                val flagedPerson = personService.flagPerson(id)
                if (flagedPerson.isRight){
                    return okServerResponse(flowOf(flagedPerson.get()))
                }
                else return notFoundServerResponse("PersonNotExist")
            } catch (exception: Exception) {
                return badRequestServerResponse("BadPersonInRequest")
            }
        }

        suspend fun countAllUsers() =
            okServerResponseCount(personService.countAllPerson())

        suspend fun countAllUsersByState(serverRequest: ServerRequest) =
            if (Person.PersonState.values().map { it.name }.contains(
                    serverRequest.pathVariable("state").uppercase()
                ).not()
            )
                badRequestServerResponse("statePersonProblem")
            else okServerResponseCount(
                personService.countAllPersonByState(
                    Person.PersonState.valueOf(
                        serverRequest.pathVariable("state").uppercase()
                    )
                )
            )

        suspend fun countAllUsersByTermsVersion(serverRequest: ServerRequest) =
            serverRequest.awaitBody<DateDTO>()
                .let { date ->
                    val dateTime = date.toLocalDate()
                    if (dateTime.isLeft)
                        badRequestServerResponse("DateError")
                    else
                        okServerResponseCount(
                            personService
                                .countAllPersonByTermsVersion(date.toLocalDate().get())
                        )
                }

        suspend fun countAllUsersByIsFraud(serverRequest: ServerRequest) =
            okServerResponseCount(
                personService.countAllPersonByFraudster(
                    serverRequest.pathVariable("isFraud").toBoolean()
                )
            )

        suspend fun countAllUsersByCountry(serverRequest: ServerRequest) =
            if (countryService.getCountryByCode(
                    serverRequest
                        .pathVariable("country")
                        .uppercase()
                )
                    .isPresent
            )
                okServerResponseCount(
                    personService
                        .countAllPersonByCountry(
                            serverRequest
                                .pathVariable("country")
                                .uppercase()
                        )
                )
            else badRequestServerResponse("CountryCodeError")


        private suspend fun okServerResponseCount(number: Long) =
            ok()
                .bodyAndAwait(
                    flowOf(number)
                )

        private suspend fun okServerResponse(persons: Flow<Person>) =
            ok()
                .bodyAndAwait(
                    persons.map { it.toPersonDTO() }
                )

        private suspend fun badRequestServerResponse(error: String) =
            badRequest()
                .header(
                    "error",
                    headerErrorInBadRequestError(error)
                )
                .buildAndAwait()

        private suspend fun notFoundServerResponse(error: String) =
            notFound()
                .header(
                    "notFound",
                    headerErrorInBadRequestError(error)
                )
                .buildAndAwait()

        private fun headerErrorInBadRequestError(string: String) =
            messageSource.getMessage(string, null, Locale.US)

    }