package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.listener

import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.handler.PersonQueueHandler
import java.util.function.Consumer

@Configuration
class PersonQueueListener(
    private val personQueueHandler: PersonQueueHandler
) {

    @Bean
    fun flagPersonTransactionQueueListener() =
        Consumer { message: Message<String> ->
            runBlocking {
                personQueueHandler.flagPersonHandler(
                    message.payload
                )
            }
        }
}