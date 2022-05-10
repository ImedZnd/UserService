package tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model

import io.vavr.control.Either
import java.util.*

class Country private constructor(
    val code: String,
    val name: String,
    val codeISO: String,
    val numCode: Int,
    val phoneCode: Int,
) {

    companion object Builder {

        fun of(
            code: String,
            name: String,
            codeISO: String,
            numCode: Int,
            phoneCode: Int,
        ) =
            checkCountry(
                code,
                name,
                codeISO,
                numCode,
                phoneCode,
            ).let {
                checkCountryErrors(
                    it,
                    code,
                    name,
                    codeISO,
                    numCode,
                    phoneCode,
                )
            }

        private fun checkCountryErrors(
            countryErrors: Sequence<CountryError> = emptySequence(),
            code: String,
            name: String,
            codeISO: String,
            numCode: Int,
            phoneCode: Int
        ): Either<Sequence<CountryError>, Country> =
            if (countryErrors.count() == 0)
                Either.right(
                    Country(
                        code,
                        name,
                        codeISO,
                        numCode,
                        phoneCode,
                    )
                )
            else
                Either.left(countryErrors)


        private fun checkCountry(
            code: String,
            name: String,
            codeISO: String,
            numCode: Int,
            phoneCode: Int
        ): Sequence<CountryError> =
            sequenceOf(
                checkCode(code),
                checkName(name),
                checkCodeISO(codeISO),
                checkNumCode(numCode),
                checkPhoneCode(phoneCode),
            )
                .filter { it.isPresent }
                .map { it.get() }

        private fun checkPhoneCode(phoneCode: Int) =
            checkField(
                phoneCode,
                CountryError.CountryPhoneCodeError
            ) { phoneCode > 0 }

        private fun checkNumCode(numCode: Int) =
            checkField(
                numCode,
                CountryError.CountryNumCodeError
            ) { numCode > 0 }

        private fun checkCodeISO(codeISO: String) =
            checkField(
                codeISO,
                CountryError.CountryCode3Error,
            ) { codeISO.length == 3 }

        private fun checkName(name: String) =
            checkField(
                name,
                CountryError.CountryNameError,
                String::isNotEmpty
            )

        private fun checkCode(code: String) =
            checkField(
                code,
                CountryError.CountryCodeError,
            ) { code.length == 2 }

        private fun <T> checkField(
            field: T,
            error: CountryError,
            validCondition: (T) -> Boolean
        ) =
            if (validCondition(field))
                Optional.empty()
            else
                Optional.of(error)

    }

    sealed interface CountryError {
        object CountryCodeError : CountryError
        object CountryNameError : CountryError
        object CountryCode3Error : CountryError
        object CountryNumCodeError : CountryError
        object CountryPhoneCodeError : CountryError
    }
}



