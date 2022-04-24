package tn.keyrus.pfe.imdznd.userservice.dirtyworld.model

import io.vavr.control.Either
import java.time.DateTimeException
import java.time.LocalDate

data class DateDTO(
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
) {
    companion object Builder {
        fun LocalDate.toDateDTO() =
            LocalDate.of(
                this.year,
                this.month,
                this.dayOfMonth
            )
    }

    fun toLocalDate():Either<DateConversionError,LocalDate> =
         try {
             Either.right(
                LocalDate.of(
                    year,
                    month,
                    day
                )
            )
        }catch (dateTimeException: DateTimeException){
            Either.left(DateConversionError)
        }
object DateConversionError
}