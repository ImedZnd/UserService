package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.handler

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service.CountryService
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.*
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO.Companion.toPersonDTO
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
            val foundPerson = personService.getPersonByID(id)
            if(foundPerson.isEmpty)
                return notFoundServerResponse()
            return okServerResponseOneElement(foundPerson.get())
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
                    if (yearRangeCheck.get() is YearRangeDTO.YearRageError.EndYearBeforeStartYearError)
                        yearRangeCheck.get().badRequestError("EndDateBeforeStartDateError")
                    else
                        yearRangeCheck.get().badRequestError("YearIsNotValidError")
                else okServerResponse(
                    personService.getAllPersonByBirthYearBetween(
                        yearRange.startYear.toYear().get(),
                        yearRange.endYear.toYear().get()
                    )
                )
            }

    private suspend fun YearRangeDTO.YearRageError.badRequestError(error: String) =
        badRequest()
            .header(
                "error",
                headerErrorInBadRequestError(error)
            )
            .buildAndAwait()

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

    private suspend fun yearOrErrorOfEitherBirthYearAfter(either: Either<YearDTO.YearConversionError, Year>) =
        if (either.isLeft)
            badRequestServerResponse("YearConversionError")
        else okServerResponse(
            personService.getAllPersonByBirthYearAfter(either.get())
        )

    private suspend fun extractYearFromServerRequest(serverRequest: ServerRequest) =
        serverRequest
            .awaitBody<YearDTO>()
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
        val checkingInteger = checkInt(serverRequest)
        return if (checkingInteger.isEmpty)
            badRequestServerResponse("errorInNumberOfFlags")
        else okServerResponse(personService.getAllPersonByNumberOfFlags(checkingInteger.get()))
    }


    suspend fun getAllPersonsNumberOfFlagsLessThan(serverRequest: ServerRequest): ServerResponse {
        val checkingInteger = checkInt(serverRequest)
        return if (checkingInteger.isEmpty)
            badRequestServerResponse("errorInNumberOfFlags")
        else okServerResponse(personService.getAllPersonByNumberOfFlagsLessThan(checkingInteger.get()))
    }


    suspend fun getAllPersonsNumberOfFlagsGreaterThan(serverRequest: ServerRequest): ServerResponse {
        val checkingInteger = checkInt(serverRequest)
        return if (checkingInteger.isEmpty)
            badRequestServerResponse("errorInNumberOfFlags")
        else okServerResponse(personService.getAllPersonByNumberOfFlagsGreaterThan(checkingInteger.get()))
    }


    private fun checkInt(serverRequest: ServerRequest): Optional<Int> {
        return try {
            val numberOfFlags = serverRequest.pathVariable("numberOfFlags").toInt()
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
                if (dateRangeCheck.isPresent) {
                    if (dateRangeCheck.get() is DateRangeDTO.DateRangeError.DateIsNotValidError)
                        dateRangeCheck.get().badRequestError("DateIsNotValidError")
                    else
                        dateRangeCheck.get().badRequestError("EndDateBeforeStartDateError")
                } else
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
                        okServerResponseOneElement(personSaved.get())
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
                        okServerResponseOneElement(personUpdated.get())
                    } else {
                        personNotFoundError()
                    }
                }
                return badRequestServerResponse("BadPersonInRequest")
            }
    }

    suspend fun deletePerson(serverRequest: ServerRequest): ServerResponse {
        return try {
            val id = serverRequest.awaitBody<PersonIdDTO>().id
            val deletedPerson = personService.deletePerson(id)
            if (deletedPerson.isRight) {
                okServerResponseOneElement(deletedPerson.get())
            } else personNotFoundError()
        } catch (exception: Exception) {
            badRequestServerResponse("BadPersonIdInRequest")
        }
    }

    suspend fun flagPerson(serverRequest: ServerRequest): ServerResponse {
        return try {
            val id = serverRequest.awaitBody<PersonIdDTO>().id
            val flagedPerson = personService.flagPerson(id)
            personToFlagOrFraudOrUnFraud(flagedPerson)
        } catch (exception: Exception) {
            badRequestServerResponse("BadPersonIdInRequest")
        }
    }

    suspend fun fraudPerson(serverRequest: ServerRequest): ServerResponse {
        return try {
            val id = serverRequest.awaitBody<PersonIdDTO>().id
            val fraudedPerson = personService.fraudPerson(id)
            personToFlagOrFraudOrUnFraud(fraudedPerson)
        } catch (exception: Exception) {
            badRequestServerResponse("BadPersonIdInRequest")
        }
    }

    suspend fun unFraudPerson(serverRequest: ServerRequest): ServerResponse {
        return try {
            val id = serverRequest.awaitBody<PersonIdDTO>().id
            val fraudedPerson = personService.unFraudPerson(id)
            personToFlagOrFraudOrUnFraud(fraudedPerson)
        } catch (exception: Exception) {
            badRequestServerResponse("BadPersonIdInRequest")
        }
    }

    private suspend fun personToFlagOrFraudOrUnFraud(person: Either<PersonService.PersonServiceError, Person>): ServerResponse =
        if (person.isLeft) {
            if (person.left is PersonService.PersonServiceError.PersonFraudsterServiceError) {
                badRequestServerResponse("PersonFraudsterServiceError")
            }
            if (person.left is PersonService.PersonServiceError.PersonServicePersonNotExistError)
                personNotFoundError()
            else badRequestServerResponse("PersonFraudsterServiceError")
        } else
            okServerResponseOneElement(person.get())

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

    private suspend fun DateRangeDTO.DateRangeError.badRequestError(error: String): ServerResponse {
        return badRequest()
            .header(
                "error",
                headerErrorInBadRequestError(error)
            )
            .buildAndAwait()
    }

    private suspend fun personNotFoundError() =
        notFoundServerResponse()

    private suspend fun okServerResponseCount(number: Int) =
        ok()
            .bodyValueAndAwait(
                number
            )

    private suspend fun okServerResponse(persons: Flow<Person>) =
        ok()
            .bodyAndAwait(
                persons.map { it.toPersonDTO() }
            )

    private suspend fun okServerResponseOneElement(persons: Person) =
        ok()
            .bodyValueAndAwait(
                persons.toPersonDTO()
            )

    private suspend fun badRequestServerResponse(error: String) =
        badRequest()
            .header(
                "error",
                headerErrorInBadRequestError(error)
            )
            .buildAndAwait()

    private suspend fun notFoundServerResponse() =
        notFound()
            .header(
                "notFound",
                headerErrorInBadRequestError("PersonNotExist")
            )
            .buildAndAwait()

    private fun headerErrorInBadRequestError(string: String) =
        try {
            messageSource.getMessage(string, null, Locale.US)
        } catch (exception: NoSuchMessageException) {
            "No Such Message Exception Raised"
        }
}