package tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.yearrange

import java.time.Year
import java.util.Optional

data class YearRangeDTO(
    val startYear: Int,
    val endYear: Int
) {

    fun checkStartYearAndEndYear() =
        when {
            (checkInvalidIntYear(this.startYear) or checkInvalidIntYear(this.endYear)) ->
                Optional.of(YearRageError.YearNotValidError)
            (this.startYear > this.endYear) ->
                Optional.of(YearRageError.EndYearBeforeStartYearError)
            else -> Optional.empty()
        }

    private fun checkInvalidIntYear(year: Int) =
        ((year < 0) or (Year.of(year).isAfter(Year.now())))

    sealed interface YearRageError {
        object YearNotValidError : YearRageError
        object EndYearBeforeStartYearError : YearRageError
    }
}