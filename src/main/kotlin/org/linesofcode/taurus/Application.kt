package org.linesofcode.taurus

import org.apache.kafka.clients.admin.NewTopic
import org.linesofcode.taurus.domain.IdentityChangeEvent
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class TaurusApplication

fun main(args: Array<String>) {
	runApplication<TaurusApplication>(*args)
}

@Configuration
class DefaultConfiguration() {
	@Bean
	fun staffOrgNodeChangeTopic(): NewTopic {
		return NewTopic(OrgNodeChangeEvent.TOPIC_NAME, 1, 1)
	}

	@Bean
	fun identityChangeTopic(): NewTopic {
		return NewTopic(IdentityChangeEvent.TOPIC_NAME, 1, 1)
	}

}