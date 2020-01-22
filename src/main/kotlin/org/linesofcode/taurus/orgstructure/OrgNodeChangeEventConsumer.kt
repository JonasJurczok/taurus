package org.linesofcode.taurus.orgstructure

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrgNodeChangeEventConsumer {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["staff-org-node-change"], groupId = "taurus-org-node-change-consumer")
    fun processMessage(message: String) {
        logger.info("got message: {}", message)
    }

}