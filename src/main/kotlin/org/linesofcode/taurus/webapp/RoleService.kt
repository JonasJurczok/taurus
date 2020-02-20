package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.IdentityRole
import org.linesofcode.taurus.domain.IdentityRoleChangeEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoleService {

    @Autowired
    private lateinit var identityRoleTemplate: KafkaTemplate<UUID, IdentityRoleChangeEvent>

    fun createIdentityRole(identityRole: IdentityRole) {

    }
}