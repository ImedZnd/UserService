package tn.keyrus.pfe.imdznd.userservice.dirtyworld.model

import io.vavr.control.Either
import java.time.Year

data class YearDTO(
    val year: Int
) {
    companion object Builder {
        fun Year.toYearDTO() =
            YearDTO(
                this.value
            )
    }

    fun toYear(): Either<YearConversionError, Year> {
         if (year<0)
             return Either.left(YearConversionError)
        return try {
            Either.right(
                Year.of(year)
            )
        } catch (exception: Exception) {
            Either.left(YearConversionError)
        }
    }

    fun checkYear(): Either<YearConversionError,YearDTO> =
        if (this.toYear().isLeft){
            Either.left(YearConversionError)
        }else Either.right(this)

    object YearConversionError
}