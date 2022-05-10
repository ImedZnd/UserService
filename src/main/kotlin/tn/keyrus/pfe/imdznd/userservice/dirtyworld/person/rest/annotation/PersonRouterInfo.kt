package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.annotation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.*
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dto.PersonDTO
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.handler.PersonHandler

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@RouterOperations(
    RouterOperation(
        path = "/person/all",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersons",
        operation = Operation(
            operationId = "getAllPersons",
            method = "GET",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all person successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/person/birthYear",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonsByBirthYear",
        operation = Operation(
            operationId = "getAllPersonsByBirthYear",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By Birth Year successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Year in Params.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = YearDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/birthYearBefore",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonsByBirthYearBefore",
        operation = Operation(
            operationId = "getAllPersonsByBirthYearBefore",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By Birth Year Before successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Year in Params.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = YearDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/birthYearAfter",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonsByBirthYearAfter",
        operation = Operation(
            operationId = "getAllPersonsByBirthYearAfter",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By Birth Year After successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Year in Params.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = YearDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/birthYearBetween",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonsByBirthYearBetween",
        operation = Operation(
            operationId = "getAllPersonsByBirthYearBetween",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By Birth Year Between successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Date Range in Params.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = DateRangeDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/state/{state}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonsByState",
        operation = Operation(
            operationId = "getAllPersonsByState",
            method = "GET",
            parameters = [Parameter(
                name = "state",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By State successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad State.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ]
        )
    ),
    RouterOperation(
        path = "/person/createdDateRange",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByCreatedDateInRange",
        operation = Operation(
            operationId = "getAllPersonByCreatedDateInRange",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By Created Date In Range.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = DateRangeDTO::class
                    )
                )]
            ),
        )
    ),
    RouterOperation(
        path = "/person/termVersionInRange",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByTermsVersionBetween",
        operation = Operation(
            operationId = "getAllPersonByTermsVersionBetween",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By term Version In Range successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Date Range.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = DateRangeDTO::class
                    )
                )]
            ),
        )
    ),
    RouterOperation(
        path = "/person/termVersionBefore",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByTermVersionBefore",
        operation = Operation(
            operationId = "getAllPersonByTermVersionBefore",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By term Version In Range Before successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Date Range.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = DateDTO::class
                    )
                )]
            ),
        )
    ),
    RouterOperation(
        path = "/person/termVersionAfter",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByTermVersionAfter",
        operation = Operation(
            operationId = "getAllPersonByTermVersionAfter",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By term Version In Range After successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Date Range.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = DateDTO::class
                    )
                )]
            ),
        )
    ),
    RouterOperation(
        path = "/person/phoneCountry/{phoneCountry}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByPhoneCountry",
        operation = Operation(
            operationId = "getAllPersonByPhoneCountry",
            method = "GET",
            parameters = [Parameter(
                name = "phoneCountry",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By phoneCountry successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad phoneCountry.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ]
        )
    ),
    RouterOperation(
        path = "/person/KYC/{KYC}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByKYC",
        operation = Operation(
            operationId = "getAllPersonByKYC",
            method = "GET",
            parameters = [Parameter(
                name = "KYC",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By KYC successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad KYC.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ]
        )
    ),
    RouterOperation(
        path = "/person/hasEmail/{hasEmail}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByHasEmail",
        operation = Operation(
            operationId = "getAllPersonByHasEmail",
            method = "GET",
            parameters = [Parameter(
                name = "hasEmail",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By hasEmail successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/person/isFraud/{isFraud}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByIsFraud",
        operation = Operation(
            operationId = "getAllPersonByIsFraud",
            method = "GET",
            parameters = [Parameter(
                name = "isFraud",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By isFraud successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/person/numberOfFlags/{numberOfFlags}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonsNumberOfFlags",
        operation = Operation(
            operationId = "getAllPersonsNumberOfFlags",
            method = "GET",
            parameters = [Parameter(
                name = "numberOfFlags",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By numberOfFlags successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "error In Number Of Flags.",
                    content = [Content(schema = Schema(implementation = String::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/person/numberOfFlagsLessThan/{numberOfFlags}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonsNumberOfFlagsLessThan",
        operation = Operation(
            operationId = "getAllPersonsNumberOfFlagsLessThan",
            method = "GET",
            parameters = [Parameter(
                name = "numberOfFlags",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By numberOfFlags successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "error In Number Of Flags.",
                    content = [Content(schema = Schema(implementation = String::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/person/numberOfFlagsGreaterThan/{numberOfFlags}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonsNumberOfFlagsGreaterThan",
        operation = Operation(
            operationId = "getAllPersonsNumberOfFlagsGreaterThan",
            method = "GET",
            parameters = [Parameter(
                name = "numberOfFlags",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By numberOfFlags successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "error In Number Of Flags.",
                    content = [Content(schema = Schema(implementation = String::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/person/countAllUsersByCountry/{country}",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "countAllUsersByCountry",
        operation = Operation(
            operationId = "countAllUsersByCountry",
            method = "GET",
            parameters = [Parameter(
                name = "country",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "count All Person By country successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Country Code Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/person/createdDateBefore",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByCreatedDateBefore",
        operation = Operation(
            operationId = "getAllPersonByCreatedDateBefore",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By Created Date Before successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Date Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = DateDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/createdDateAfter",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByCreatedDateAfter",
        operation = Operation(
            operationId = "getAllPersonByCreatedDateAfter",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By Created Date After successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Date Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = DateDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/fraudAndCountryCode",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getAllPersonByFraudsterAndCountryCode",
        operation = Operation(
            operationId = "getAllPersonByFraudsterAndCountryCode",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get All Person By Fraudster And Country Code successfully.",
                    content = [Content(schema = Schema(implementation = PersonDTO::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Country Code Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = FraudsterAndCountryCodeDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/countAllPerson",
        method = [RequestMethod.GET],
        beanClass = PersonHandler::class,
        beanMethod = "countAllUsers",
        operation = Operation(
            operationId = "countAllUsers",
            method = "GET",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "count all person successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/person/countAllUsersByTermsVersion",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "countAllUsersByTermsVersion",
        operation = Operation(
            operationId = "countAllUsersByTermsVersion",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "count All Person By Terms Version successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Date Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = FraudsterAndCountryCodeDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/save",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "savePerson",
        operation = Operation(
            operationId = "savePerson",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "save Person successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Person Exist.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Person In Request.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = PersonDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/update",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "updatePerson",
        operation = Operation(
            operationId = "updatePerson",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "update Person successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                ),
                ApiResponse(
                    responseCode = "404",
                    description = "Person not Exist.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Person In Request.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = PersonDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/delete",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "deletePerson",
        operation = Operation(
            operationId = "deletePerson",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "delete Person successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                ),
                ApiResponse(
                    responseCode = "404",
                    description = "Person not Exist.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Person In Request.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = PersonIdDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/flag",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "flagPerson",
        operation = Operation(
            operationId = "flagPerson",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "delete Person successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                ),
                ApiResponse(
                    responseCode = "404",
                    description = "Person not Exist.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Person In Request.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Person Fraudster Service Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = PersonIdDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/id",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "getPersonById",
        operation = Operation(
            operationId = "getPersonById",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "delete Person successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                ),
                ApiResponse(
                    responseCode = "404",
                    description = "Person not Exist.",
                    content = [Content(schema = Schema(implementation = String::class))]
                )
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = PersonIdDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/fraud",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "fraudPerson",
        operation = Operation(
            operationId = "fraudPerson",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "delete Person successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                ),
                ApiResponse(
                    responseCode = "404",
                    description = "Person not Exist.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Person Fraudster Service Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = PersonIdDTO::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/person/unfraud",
        method = [RequestMethod.POST],
        beanClass = PersonHandler::class,
        beanMethod = "unFraudPerson",
        operation = Operation(
            operationId = "unFraudPerson",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "delete Person successfully.",
                    content = [Content(schema = Schema(implementation = Int::class))]
                ),
                ApiResponse(
                    responseCode = "404",
                    description = "Person not Exist.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Person Fraudster Service Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = PersonIdDTO::class
                    )
                )]
            )
        )
    ),
)

annotation class PersonRouterInfo
