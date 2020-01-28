package org.linesofcode.taurus.redis_import

import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RedisImporter {
    private val logger = LoggerFactory.getLogger(RedisImporter::class.java)

    @Autowired
    private lateinit var hashOperations: HashOperations<String, UUID, OrgNode>

    @KafkaListener(topics = ["staff-org-node-change"], groupId = "taurus-redis-importer", id = "redis-staff-org-node-change")
    fun importOrgEvents(event: OrgNodeChangeEvent) {

        logger.info("Importing org node event [{}].", event)
        hashOperations.put("OrgNode", event.orgNode.id, event.orgNode)
    }
}