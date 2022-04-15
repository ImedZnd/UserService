package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.repository.PersonRepository
import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonDatabaseRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.repository.PersonReactiveRepository

@Configuration
class PersonConfiguration {

    @Bean
    fun personDatabaseRepository(
        reactiveDatabaseRepository: PersonReactiveRepository
    ): PersonRepository =
        PersonDatabaseRepository(reactiveDatabaseRepository)

    @Bean
    fun personService(
        personDatabaseRepository: PersonRepository
    ): PersonService =
        PersonService(personDatabaseRepository)

}