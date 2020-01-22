package org.linesofcode.taurus.infrastructure

import org.apache.kafka.common.TopicPartition
import org.slf4j.Logger
import org.springframework.kafka.listener.AbstractConsumerSeekAware
import org.springframework.kafka.listener.ConsumerSeekAware

abstract class AbstractReadFromBeginningListener: AbstractConsumerSeekAware() {
    protected abstract val logger: Logger

    override fun onPartitionsAssigned(assignments: MutableMap<TopicPartition, Long>?, callback: ConsumerSeekAware.ConsumerSeekCallback?) {
        super.onPartitionsAssigned(assignments, callback)
        assignments?.keys?.forEach { tp ->
            logger.info("SeekToBeginning for Topic [{}] and partition [{}]", tp.topic(), tp.partition())
            callback?.seekToBeginning(tp.topic(), tp.partition())
        }
    }

}