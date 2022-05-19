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
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.date.DateDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.daterange.DateRangeDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.fraudsterandcountrycode.FraudsterAndCountryCodeDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.yearrange.YearRangeDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO.Companion.toPersonDTO
import java.time.LocalDateTime
import java.time.LocalTime
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

    suspend fun getPersonById(serverRequest: ServerRequest): ServerResponse =
        getPersonIdFromServerRequestReturnPeronOrErrorIfNotExist(serverRequest)
            .let {
                if (it.isLeft)
                    it.left
                else {
                    val deletedPerson = personService.getPersonByID(it.get())
                    okServerResponseOneElement(deletedPerson.get())
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
                    returnYearRangeErrors(yearRangeCheck)
                else okServerResponse(
                    personService.getAllPersonByBirthYearBetween(
                        yearRange.startYear,
                        yearRange.endYear
                    )
                )
            }

    private suspend fun returnYearRangeErrors(dateRange: Optional<out YearRangeDTO.YearRageError>) =
        if (dateRange.get() is YearRangeDTO.YearRageError.EndYearBeforeStartYearError)
            dateRange.get().badRequestError("EndDateBeforeStartDateError")
        else
            dateRange.get().badRequestError("YearIsNotValidError")

    private suspend fun YearRangeDTO.YearRageError.badRequestError(error: String) =
        badRequest()
            .header(
                "error",
                headerErrorInBadRequestError(error)
            )
            .buildAndAwait()

    suspend fun getAllPersonsByBirthYearBefore(serverRequest: ServerRequest) =
        extractYearFromServerRequest(serverRequest)
            .let { yearOrErrorOfEitherBirthYearBefore(it) }

    suspend fun getAllPersonsByBirthYearAfter(serverRequest: ServerRequest) =
        extractYearFromServerRequest(serverRequest)
            .let { yearOrErrorOfEitherBirthYearAfter(it) }

    private suspend fun yearOrErrorOfEitherBirthYearBefore(either: Either<String, Int>): ServerResponse =
        if (either.isLeft)
            badRequestServerResponse("YearConversionError")
        else okServerResponse(
            personService.getAllPersonByBirthYearBefore(either.get())
        )

    private suspend fun yearOrErrorOfEitherBirthYearAfter(either: Either<String, Int>) =
        if (either.isLeft)
            badRequestServerResponse(either.left)
        else okServerResponse(
            personService.getAllPersonByBirthYearAfter(either.get())
        )

    private fun extractYearFromServerRequest(serverRequest: ServerRequest): Either<String, Int> =
        try {
            val year = serverRequest.pathVariable("year").toInt()
            if (year < 0) {
                Either.left("YearIsNotValidError")
            } else Either.right(year)
        } catch (exception: Exception) {
            Either.left("YearIsNotValidError")
        }

    suspend fun getAllPersonsByBirthYear(serverRequest: ServerRequest) =
        extractYearFromServerRequest(serverRequest)
            .let { yearOrErrorOfEitherBirthYear(it) }

    private suspend fun yearOrErrorOfEitherBirthYear(either: Either<String, Int>): ServerResponse =
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

    suspend fun getAllPersonByTermsVersion(serverRequest: ServerRequest) =
        serverRequest.awaitBody<DateDTO>()
            .let { dateDTO ->
                val date = dateDTO.toLocalDate()
                if (date.isLeft)
                    badRequestServerResponse("DateIsNotValidError")
                else okServerResponse(
                    personService.getAllPersonByTermsVersion(
                        date.get()
                    )
                )
            }

    suspend fun countAllPersonByBirthYear(serverRequest: ServerRequest) =
        try {
            val year = serverRequest.pathVariable("year").toInt()
            okServerResponseCount(
                personService.countByBirthYear(year)
            )
        } catch (exception: Exception) {
            badRequestServerResponse("YearIsNotValidError")
        }

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
                    returnDateErrors(dateRangeCheck)
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
                if (dateRangeCheck.isPresent)
                    returnDateErrors(dateRangeCheck)
                else
                    okServerResponse(
                        personService
                            .getAllPersonByCreatedDateInRange(
                                dateRange.startDate.toLocalDate().get(),
                                dateRange.endDate.toLocalDate().get()
                            )
                    )
            }

    private suspend fun returnDateErrors(dateRange: Optional<out DateRangeDTO.DateRangeError>) =
        if (dateRange.get() is DateRangeDTO.DateRangeError.DateIsNotValidError)
            dateRange.get().badRequestError("DateIsNotValidError")
        else
            dateRange.get().badRequestError("EndDateBeforeStartDateError")

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

    suspend fun savePerson(serverRequest: ServerRequest) =
        serverRequest.awaitBody<PersonDTO>()
            .let { person ->
                val personToSave = person.toPerson()
                if (personToSave.isRight)
                    savePeronIfNotExistOrBadRequest(personToSave.get())
                else badRequestServerResponse("BadPersonInRequest")
            }

    private suspend fun savePeronIfNotExistOrBadRequest(person: Person): ServerResponse {
        val personSaved = personService.savePerson(person)
        return if (personSaved.isRight)
            okServerResponseOneElement(personSaved.get())
        else
            badRequestServerResponse("PersonExist")
    }

    suspend fun updatePerson(serverRequest: ServerRequest) =
        serverRequest.awaitBody<PersonDTO>()
            .let { person ->
                val personToUpdate = person.toPerson()
                if (personToUpdate.isRight)
                    personService.updatePerson(personToUpdate.get())
                        .let {
                            updatePersonOrPersonNotFound(it)
                        }
                else badRequestServerResponse("BadPersonInRequest")
            }


    private suspend fun updatePersonOrPersonNotFound(either: Either<PersonService.PersonServiceError, Person>) =
        if (either.isRight)
            okServerResponseOneElement(either.get())
        else
            personNotFoundError()


    suspend fun deletePerson(serverRequest: ServerRequest): ServerResponse =
        getPersonIdFromServerRequestReturnPeronOrErrorIfNotExist(serverRequest)
            .let {
                if (it.isLeft)
                    it.left
                else {
                    val deletedPerson = personService.deletePerson(it.get())
                    if (deletedPerson.isLeft)
                        notFoundServerResponse()
                    else okServerResponseOneElement(deletedPerson.get())
                }
            }


    suspend fun flagPerson(serverRequest: ServerRequest): ServerResponse =
        getPersonIdFromServerRequestReturnPeronOrErrorIfNotExist(serverRequest)
            .let {
                if (it.isLeft)
                    it.left
                else {
                    val flagedPerson = personService.flagPerson(it.get())
                    personToFlagOrFraudOrUnFraud(flagedPerson)
                }
            }

    suspend fun fraudPerson(serverRequest: ServerRequest): ServerResponse =
        getPersonIdFromServerRequestReturnPeronOrErrorIfNotExist(serverRequest)
            .let {
                if (it.isLeft)
                    it.left
                else {
                    val fraudedPerson = personService.fraudPerson(it.get())
                    personToFlagOrFraudOrUnFraud(fraudedPerson)
                }
            }

    suspend fun unFraudPerson(serverRequest: ServerRequest): ServerResponse =
        getPersonIdFromServerRequestReturnPeronOrErrorIfNotExist(serverRequest)
            .let {
                if (it.isLeft)
                    it.left
                else {
                    val fraudedPerson = personService.unFraudPerson(it.get())
                    personToFlagOrFraudOrUnFraud(fraudedPerson)
                }
            }

    private suspend fun getPersonIdFromServerRequestReturnPeronOrErrorIfNotExist(serverRequest: ServerRequest): Either<ServerResponse, Long> =
        try {
            val personId = serverRequest.pathVariable("personId").toLong()
            val person = personService.getPersonByID(personId)
            if (person.isEmpty)
                Either.left(notFoundServerResponse())
            else Either.right(personId)
        } catch (exception: Exception) {
            Either.left(badRequestServerResponse("BadPersonIdInPath"))
        }

    private suspend fun personToFlagOrFraudOrUnFraud(person: Either<PersonService.PersonServiceError, Person>): ServerResponse =
        if (person.isLeft) {
            when (person.left) {
                is PersonService.PersonServiceError.PersonFraudsterServiceError ->
                    badRequestServerResponse("PersonFraudsterServiceError")
                is PersonService.PersonServiceError.PersonServicePersonNotExistError ->
                    personNotFoundError()
                else -> badRequestServerResponse("PersonFraudsterServiceError")
            }
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