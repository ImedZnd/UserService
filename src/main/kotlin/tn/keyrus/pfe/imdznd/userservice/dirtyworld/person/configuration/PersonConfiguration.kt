package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.configuration

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tn.keyrus.pfe.imdznd.userservice.cleanworld.country.service.CountryService
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.handler.PersonQueueHandler
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.setting.PersonQueueSetting
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonDatabaseRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonReactiveRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.rest.handler.PersonHandler

@Configuration
class PersonConfiguration {

    @Bean
    fun personDatabaseRepository(
        reactiveDatabaseRepository: PersonReactiveRepository,
        @Autowired  rabbitTemplate: RabbitTemplate,
        personQueueSetting: PersonQueueSetting
    ): PersonRepository =
        PersonDatabaseRepository(reactiveDatabaseRepository,rabbitTemplate,personQueueSetting)

    @Bean
    fun personService(
        personDatabaseRepository: PersonRepository
    ): PersonService =
        PersonService(personDatabaseRepository)

    @Bean
    fun personHandler(
        personService: PersonService,
        countryService: CountryService,
        @Autowired messageSource: MessageSource
    ): PersonHandler =
        PersonHandler(personService,countryService,messageSource)

    @Bean
    fun personQueueHandler(
        personService: PersonService,
    ): PersonQueueHandler =
        PersonQueueHandler(personService,)

}
