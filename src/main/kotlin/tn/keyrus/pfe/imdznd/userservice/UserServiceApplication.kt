package tn.keyrus.pfe.imdznd.userservice

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@ComponentScan("tn.keyrus.pfe.imdznd.userservice.**")
@SpringBootApplication
@EnableR2dbcRepositories("tn.keyrus.pfe.imdznd.userservice.**")
@OpenAPIDefinition(info = Info(title = "User Service", version = "1.0", description = "Documentation APIs v1.0"))
@ConfigurationPropertiesScan
class UserServiceApplication

fun main(args: Array<String>) {
    runApplication<UserServiceApplication>(*args)
}
