package org.linesofcode.taurus.orgstructure

import org.apache.kafka.common.TopicPartition
import org.linesofcode.taurus.infrastructure.AbstractReadFromBeginningListener
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AbstractConsumerSeekAware
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.stereotype.Component

@Component
class OrgNodeChangeEventConsumer: AbstractReadFromBeginningListener() {
    override val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["staff-org-node-change"], groupId = "taurus-org-node-change-consumer")
    fun processMessage(event: OrgNodeChangeEvent) {
        logger.info("got message: {}", event)
    }
}