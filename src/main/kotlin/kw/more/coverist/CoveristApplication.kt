package kw.more.coverist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class CoveristApplication

fun main(args: Array<String>) {
	runApplication<CoveristApplication>(*args)
}
