package com.notionclone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class NotionCloneApplication

fun main(args: Array<String>) {
    runApplication<NotionCloneApplication>(*args)
}
