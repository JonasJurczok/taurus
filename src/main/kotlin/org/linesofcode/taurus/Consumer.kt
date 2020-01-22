package org.linesofcode.taurus

import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AbstractConsumerSeekAware
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.stereotype.Component

@Component
class KotlinConsumer: AbstractConsumerSeekAware() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onPartitionsAssigned(assignments: MutableMap<TopicPartition, Long>?, callback: ConsumerSeekAware.ConsumerSeekCallback?) {
        super.onPartitionsAssigned(assignments, callback)
        assignments?.keys?.forEach { tp ->
            logger.info("SeekToBeginning for Topic [{}] and partition [{}]", tp.topic(), tp.partition())
            callback?.seekToBeginning(tp.topic(), tp.partition())
        }
    }

    @KafkaListener(topics = ["simple-message-topic"], groupId = "simple-kotlin-consumer")
    fun processMessage(message: String) {
        logger.info("got message: {}", message)
    }
}