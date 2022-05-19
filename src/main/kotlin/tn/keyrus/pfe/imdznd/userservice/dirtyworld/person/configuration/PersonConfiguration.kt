package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.configuration

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service.CountryService
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.handler.PersonQueueHandler
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.publisher.PersonQueuePublisher
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.setting.PersonQueueSetting
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonDatabaseRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonReactiveRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.handler.PersonHandler

@Configuration
class PersonConfiguration {

    @Bean
    fun personDatabaseRepository(
        reactiveDatabaseRepository: PersonReactiveRepository,
    ): PersonRepository =
        PersonDatabaseRepository(reactiveDatabaseRepository)

    @Bean
    fun personService(
        personDatabaseRepository: PersonRepository,
        personQueuePublisher: PersonQueuePublisher
    ) =
        PersonService(
            personDatabaseRepository,
            personQueuePublisher
        )

    @Bean
    fun personHandler(
        personService: PersonService,
        countryService: CountryService,
        messageSource: MessageSource
    ) =
        PersonHandler(
            personService,
            countryService,
            messageSource
        )

    @Bean
    fun personQueueHandler(
        personService: PersonService,
    ) =
        PersonQueueHandler(personService)

    @Bean
    fun personQueuePublisher(
        rabbitTemplate: RabbitTemplate,
        personQueueSetting: PersonQueueSetting
    ) =
        PersonQueuePublisher(
            rabbitTemplate,
            personQueueSetting
        )

}
