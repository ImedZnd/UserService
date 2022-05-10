package tn.keyrus.pfe.imdznd.userservice.dirtyworld.person.dao

import tn.keyrus.pfe.imdznd.userservice.cleanworld.person.model.Person

data class PersonInCountry(
    val countryCode: String,
    val persons: Collection<Person>
)