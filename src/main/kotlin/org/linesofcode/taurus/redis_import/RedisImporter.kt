package org.linesofcode.taurus.redis_import

import org.linesofcode.taurus.domain.Identity
import org.linesofcode.taurus.domain.IdentityChangeEvent
import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.IDENTITY_KEY
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.ORG_NODE_KEY
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
    private lateinit var orgNodeOperations: HashOperations<String, UUID, OrgNode>

    @Autowired
    private lateinit var identityOperations: HashOperations<String, UUID, Identity>

    @KafkaListener(topics = [OrgNodeChangeEvent.TOPIC_NAME], groupId = "taurus-redis-importer-orgnode", id = "redis-staff-org-node-change")
    fun importOrgEvents(event: OrgNodeChangeEvent) {

        logger.info("Importing org node event [{}].", event)
        orgNodeOperations.put(ORG_NODE_KEY, event.orgNode.id, event.orgNode)
    }

    @KafkaListener(topics = [IdentityChangeEvent.TOPIC_NAME], groupId = "taurus-redis-importer-identity", id = "redis-identity-governance-identity-change")
    fun importIdentityEvents(event: IdentityChangeEvent) {
        logger.info("Importing identity event [{}].", event)
        identityOperations.put(IDENTITY_KEY, event.identity.id, event.identity)

    }
}