package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.Action.CREATE
import org.linesofcode.taurus.domain.Identity
import org.linesofcode.taurus.domain.IdentityChangeEvent
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.IDENTITY_KEY
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class IdentityService {

    private val logger = LoggerFactory.getLogger(IdentityService::class.java)

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<UUID, IdentityChangeEvent>

    @Autowired
    private lateinit var identityOperations: HashOperations<String, UUID, Identity>


    fun create(identity: Identity) {
        logger.info("Creating identity [{}].", identity)
        kafkaTemplate.send(IdentityChangeEvent.TOPIC_NAME, identity.id, IdentityChangeEvent(UUID.randomUUID(), CREATE, identity))
    }

    fun getById(id: UUID): Identity? {
        return identityOperations.get(IDENTITY_KEY, id)
    }

    fun getByIds(members: Set<UUID>): Set<Identity> {
        return members.mapNotNull { id -> getById(id) }.toSet()
    }

    fun getAll(): Set<Identity> {
        return identityOperations.values(IDENTITY_KEY).toHashSet()
    }

}
