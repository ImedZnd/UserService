package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.queue.handler

import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.service.PersonService

class PersonQueueHandler(
    private val personService: PersonService
) {
    suspend fun flagPersonHandler(personId: String) {
        try {
            personService.flagPerson(personId.toLong())
        } catch (exception: Exception) {
            print(exception)
        }
    }
}