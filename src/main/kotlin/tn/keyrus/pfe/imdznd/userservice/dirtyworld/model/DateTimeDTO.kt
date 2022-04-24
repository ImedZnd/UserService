package tn.keyrus.pfe.imdznd.userservice.dirtyworld.model

import io.vavr.control.Either
import java.time.LocalDateTime

data class DateTimeDTO(
    private val year: Int,
    private val monthValue: Int,
    private val dayOfMonth: Int,
    private val hour: Int,
    private val minute: Int,
    private val second: Int,
    private val nano: Int,
) { companion object Builder {
        fun LocalDateTime.toDateTimeDTO() =
            DateTimeDTO(
                this.year,
                this.monthValue,
                this.dayOfMonth,
                this.hour,
                this.minute,
                this.second,
                this.nano
            )
    }
    fun toLocalDateTime(): Either<DateTimeConversionError, LocalDateTime> =
        try {
            Either.right(
                LocalDateTime.of(
                    year,
                    monthValue,
                    dayOfMonth,
                    hour,
                    minute,
                    second,
                    nano
                    )
            )
        } catch (exception: Exception) {
            println("***********")
            print(exception)
            println("***********")
            Either.left(DateTimeConversionError)
        }

    object DateTimeConversionError
}