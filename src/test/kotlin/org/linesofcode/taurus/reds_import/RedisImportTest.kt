package org.linesofcode.taurus.reds_import

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.linesofcode.taurus.domain.OrgNodeChangeEvent.OrgNodeAction.CREATE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.core.KafkaTemplate
import java.util.UUID

@SpringBootTest
class RedisImportTest {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<UUID, OrgNodeChangeEvent>

    @Autowired
    private lateinit var orgNodeTemplate: RedisTemplate<String, OrgNode>

    @Autowired
    private lateinit var orgNodeOperations: HashOperations<String, UUID, OrgNode>

    @BeforeEach
    fun initialize() {
        cleanup()
    }

    @AfterEach
    fun cleanup() {
        orgNodeTemplate.execute<Any> { con -> con.flushAll() }
    }

    @Test
    fun `OrgNode should show up in redis`() {

        val event = OrgNodeChangeEvent(UUID.randomUUID(), CREATE, OrgNode(UUID.randomUUID(), "Root node"))

        kafkaTemplate.send("staff-org-node-change", event)

        Thread.sleep(1000)

        val node = orgNodeOperations.get("OrgNode", event.orgNode.id)

        assertTrue(node != null)
        assertEquals(event.orgNode.name, node?.name)
    }
}