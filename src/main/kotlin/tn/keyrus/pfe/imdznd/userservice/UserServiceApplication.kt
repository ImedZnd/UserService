package tn.keyrus.pfe.imdznd.userservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@ComponentScan("tn.keyrus.pfe.imdznd.userservice.**")
@SpringBootApplication
@EnableR2dbcRepositories("tn.keyrus.pfe.imdznd.userservice.**")
@ConfigurationPropertiesScan
class UserServiceApplication

fun main(args: Array<String>) {
    runApplication<UserServiceApplication>(*args)
}
