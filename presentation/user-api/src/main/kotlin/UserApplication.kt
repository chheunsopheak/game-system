package com.gamesystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(
    scanBasePackages = [
        "com.gamesystem",
        "common",
        "service",
        "config",
        "filter",
        "jwt",
        "auth",
        "seeder",
        "network",
    ]
)
@EntityScan("entity")
@EnableJpaRepositories("repository")
@EnableJpaAuditing
class UserApplication

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args)
}