package tn.keyrus.pfe.imdznd.userservice.dirtyworld.model.dto.localdate

import io.vavr.control.Either
import java.time.LocalDateTime
import java.time.Month

data class LocalDateDTO(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
) {
    companion object {
        fun LocalDateTime.toLocalDateDTO() =
            LocalDateDTO(
                year,
                month.value,
                dayOfMonth,
                hour,
                minute,
                second,
                nano
            )
    }

    fun toLocalDateTime(): Either<out LocalDateTimeDTOError, LocalDateTime> =
        try {
            val date =
                LocalDateTime.of(
                    year,
                    Month.of(month),
                    day,
                    hour,
                    minute,
                    second,
                    nano
                )
            if (date.isAfter(LocalDateTime.now()))
                Either.left<LocalDateTimeDTOError, LocalDateTime>(LocalDateTimeDTOError.LocalDateTimeInTheFutureError)
            else Either.right(date)
        } catch (exception: Exception) {
            Either.left(LocalDateTimeDTOError.LocalDateTimeConversionError)
        }

    sealed interface LocalDateTimeDTOError {
        object LocalDateTimeConversionError : LocalDateTimeDTOError
        object LocalDateTimeInTheFutureError : LocalDateTimeDTOError
    }
}