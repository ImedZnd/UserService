package tn.keyrus.pfe.imdznd.userservice.dirtyworld.model

import java.util.Optional

data class YearRangeDTO(
    val startYear: YearDTO,
    val endYear: YearDTO
) {

    fun checkStartYearAndEndYear() =
        when {
            this.startYear.toYear().isLeft or this.endYear.toYear().isLeft -> Optional.of(YearRageError.YearNotValidError)
            this.startYear.toYear().get()
                .isAfter(this.endYear.toYear().get()) -> Optional.of(YearRageError.EndYearBeforeStartYearError)
            else -> Optional.empty()
        }

    sealed interface YearRageError {
        object YearNotValidError : YearRageError
        object EndYearBeforeStartYearError : YearRageError
    }
}