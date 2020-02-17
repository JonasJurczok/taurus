package org.linesofcode.taurus.webapp

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.linesofcode.taurus.redis_import.RedisConfig
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.eq
import org.mockito.internal.verification.Times
import org.springframework.data.redis.core.HashOperations
import org.springframework.kafka.core.KafkaTemplate
import java.util.UUID
import kotlin.test.assertFailsWith


class OrgNodeServiceTest {
    val template = Mockito.mock(KafkaTemplate::class.java) as KafkaTemplate<UUID, OrgNodeChangeEvent>
    val operations = Mockito.mock(HashOperations::class.java) as HashOperations<String, UUID, OrgNode>
    val orgNodeService = OrgNodeService(template, operations)

    @AfterEach
    fun cleanup() {
        Mockito.reset(template, operations)
    }

    @Test
    fun `saving for version higher than the current one should fail`() {
        val node = OrgNode(name = "Test", version = 1)
        Mockito.`when`(operations.get(RedisConfig.ORG_NODE_KEY, node.id)).thenReturn(node)

        val update = OrgNode(id = node.id, name = "Test2", version = 2)
        assertFailsWith(IllegalStateException::class) { orgNodeService.createOrUpdate(update) }
    }

    @Test
    fun `saving for version lower than the current one should fail`() {
        val node = OrgNode(name = "Test", version = 1)
        Mockito.`when`(operations.get(RedisConfig.ORG_NODE_KEY, node.id)).thenReturn(node)

        val update = OrgNode(id = node.id, name = "Test2", version = 0)
        assertFailsWith(IllegalStateException::class) { orgNodeService.createOrUpdate(update) }
    }

    @Test
    fun `creating with non existing node should work`() {
        val node = OrgNode(name = "Test", version = 1)
        Mockito.`when`(operations.get(RedisConfig.ORG_NODE_KEY, node.id)).thenReturn(null)

        orgNodeService.createOrUpdate(node)
        Mockito.verify(template, Mockito.times(1)).send(eq(OrgNodeChangeEvent.TOPIC_NAME), eq(node.id) , any())
    }
}