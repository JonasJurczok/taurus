package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.Action.CREATE
import org.linesofcode.taurus.domain.IdentityRole
import org.linesofcode.taurus.domain.IdentityRoleChangeEvent
import org.linesofcode.taurus.domain.IdentityRoleChangeEvent.Companion.TOPIC_NAME
import org.linesofcode.taurus.redis_import.RedisConfig
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.IDENTITY_ROLE_KEY
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoleService(@Autowired val identityRoleTemplate: KafkaTemplate<UUID, IdentityRoleChangeEvent>,
                  @Autowired val identityRoleOperations: HashOperations<String, UUID, Map<UUID, IdentityRole>>) {

    private val logger = LoggerFactory.getLogger(RoleService::class.java)

    fun createIdentityRole(identityRole: IdentityRole) {
        logger.info("Creating identityRole [$identityRole].")
        identityRoleTemplate.send(TOPIC_NAME, identityRole.id, IdentityRoleChangeEvent(UUID.randomUUID(), CREATE, identityRole))
    }

    fun getByOrgNodeId(id: UUID): Set<IdentityRole> {
        return identityRoleOperations.get(IDENTITY_ROLE_KEY, id)?.values?.toSet() ?: emptySet()
    }
}