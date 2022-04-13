package tn.keyrus.pfe.imdznd.userservice.cleanworld.country.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CountryTest {

    @Test
    fun `valid test Country non empty field`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val result =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).get()
        assertAll(
            { assert(result.code == code) },
            { assert(result.name == name) },
            { assert(result.code3 == code3) },
            { assert(result.numCode == numCode) },
            { assert(result.phoneCode == phoneCode) },
        )
    }

    @Test
    fun `code country error Country empty code`() {
        val code = ""
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val result =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Country.CountryError.CountryCodeError }) },
        )
    }

    @Test
    fun `name country error Country empty code`() {
        val code = "PY"
        val name = ""
        val code3 = "pRY"
        val numCode = 600
        val phoneCode = 595
        val result =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Country.CountryError.CountryNameError }) },
        )
    }

    @Test
    fun `code3 country error Country empty code`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = ""
        val numCode = 600
        val phoneCode = 595
        val result =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Country.CountryError.CountryCode3Error }) },
        )
    }

    @Test
    fun `numCode country error Country empty code`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = -1
        val phoneCode = 595
        val result =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Country.CountryError.CountryNumCodeError }) },
        )
    }

    @Test
    fun `phoneCode country error Country empty code`() {
        val code = "PY"
        val name = "Paraguay"
        val code3 = "pRY"
        val numCode = 200
        val phoneCode = -1
        val result =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.all { it is Country.CountryError.CountryPhoneCodeError }) },
        )
    }

    @Test
    fun `five country error Country empty code`() {
        val code = ""
        val name = ""
        val code3 = ""
        val numCode = -2
        val phoneCode = -1
        val result =
            Country.of(
                code,
                name,
                code3,
                numCode,
                phoneCode
            ).left
        assertAll(
            { assert(result.count() == 5) },
            { assert(result.any { it is Country.CountryError.CountryCodeError }) },
            { assert(result.any { it is Country.CountryError.CountryNameError }) },
            { assert(result.any { it is Country.CountryError.CountryCode3Error }) },
            { assert(result.any { it is Country.CountryError.CountryNumCodeError }) },
            { assert(result.any { it is Country.CountryError.CountryPhoneCodeError }) },
        )
    }

}