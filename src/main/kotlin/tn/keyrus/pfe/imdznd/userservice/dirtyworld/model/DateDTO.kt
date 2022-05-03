package tn.keyrus.pfe.imdznd.userservice.dirtyworld.model

import io.vavr.control.Either
import java.time.DateTimeException
import java.time.LocalDate

data class DateDTO(
    val year: Int ,
    val monthOfYear: Int ,
    val day: Int ,
) {
    companion object Builder {
        fun LocalDate.toDateDTO() =
            DateDTO(
                year,
                month.value,
                dayOfMonth
            )
    }

    fun toLocalDate():Either<DateConversionError,LocalDate> =
         try {
             Either.right(
                LocalDate.of(
                    year,
                    monthOfYear,
                    day
                )
            )
        }catch (dateTimeException: DateTimeException){
            Either.left(DateConversionError)
        }
object DateConversionError
}