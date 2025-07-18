package com.gamesystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = ["com.gamesystem", "service"])
@EntityScan("entity")
@EnableJpaRepositories("repository")
class UserApplication

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args)
}