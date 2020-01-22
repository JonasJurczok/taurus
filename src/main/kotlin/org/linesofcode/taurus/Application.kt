package org.linesofcode.taurus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaurusApplication

fun main(args: Array<String>) {
	runApplication<TaurusApplication>(*args)
}
