package tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import tn.keyrus.pfe.imdznd.userservice.dirtyworld.country.dao.CountryDAO

interface CountryReactiveRepository : ReactiveCrudRepository<CountryDAO, String>