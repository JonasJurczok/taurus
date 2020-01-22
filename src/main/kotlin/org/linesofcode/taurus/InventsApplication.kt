package org.linesofcode.taurus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InventsApplication

fun main(args: Array<String>) {
	runApplication<InventsApplication>(*args)
}