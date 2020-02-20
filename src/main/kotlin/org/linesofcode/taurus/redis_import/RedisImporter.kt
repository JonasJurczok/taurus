package org.linesofcode.taurus.redis_import

import org.linesofcode.taurus.domain.Identity
import org.linesofcode.taurus.domain.IdentityChangeEvent
import org.linesofcode.taurus.domain.IdentityRole
import org.linesofcode.taurus.domain.IdentityRoleChangeEvent
import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.IDENTITY_KEY
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.IDENTITY_ROLE_KEY
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.ORG_NODE_KEY
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.ORG_NODE_ROOT_KEY
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AbstractConsumerSeekAware
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RedisImporter: AbstractConsumerSeekAware() {
    private val logger = LoggerFactory.getLogger(RedisImporter::class.java)

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var orgNodeOperations: HashOperations<String, UUID, OrgNode>

    @Autowired
    private lateinit var identityOperations: HashOperations<String, UUID, Identity>

    @Autowired
    private lateinit var identityRoleOperations: HashOperations<String, UUID, IdentityRole>

    @KafkaListener(topics = [OrgNodeChangeEvent.TOPIC_NAME], groupId = "taurus-redis-importer-orgnode", id = "redis-staff-org-node-change")
    fun importOrgEvents(event: OrgNodeChangeEvent) {

        logger.info("Importing org node event [{}].", event)
        val orgNode = event.orgNode

        if (orgNode.parent == null) {
            orgNodeOperations.put(ORG_NODE_ROOT_KEY, orgNode.id, orgNode)
        } else {
            orgNodeOperations.delete(ORG_NODE_ROOT_KEY, orgNode.id)
        }

        orgNodeOperations.put(ORG_NODE_KEY, orgNode.id, orgNode)
    }

    @KafkaListener(topics = [IdentityChangeEvent.TOPIC_NAME], groupId = "taurus-redis-importer-identity", id = "redis-staff-identity-change")
    fun importIdentityEvents(event: IdentityChangeEvent) {
        logger.info("Importing identity event [{}].", event)
        identityOperations.put(IDENTITY_KEY, event.identity.id, event.identity)

    }

    @KafkaListener(topics = [IdentityRoleChangeEvent.TOPIC_NAME], groupId = "taurus-redis-importer-identity-role", id = "redis-identity-governance-identity-role-change")
    fun importIdentityRoleEvents(event: IdentityRoleChangeEvent) {
        logger.info("Importing identity event [{}].", event)
        identityRoleOperations.put(IDENTITY_ROLE_KEY, event.identityRole.id, event.identityRole)

    }

    fun reload() {
        logger.info("Reloading redis.")

        redisTemplate.execute<Any> { con -> con.flushAll() }

        listOf(IdentityChangeEvent.TOPIC_NAME, OrgNodeChangeEvent.TOPIC_NAME)
                .mapNotNull { topicName -> seekCallbacks.keys.find { it.topic() == topicName } }
                .map { getSeekCallbackFor(it)?.seekToBeginning(it.topic(), it.partition()) }
    }
}