package org.linesofcode.taurus.reds_import

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.linesofcode.taurus.domain.Action.CREATE
import org.linesofcode.taurus.domain.Action.UPDATE
import org.linesofcode.taurus.domain.Identity
import org.linesofcode.taurus.domain.Identity.IdentityType.HUMAN
import org.linesofcode.taurus.domain.IdentityChangeEvent
import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.core.KafkaTemplate
import java.util.UUID

@SpringBootTest
class RedisImportTest {

    private val logger = LoggerFactory.getLogger(RedisImportTest::class.java)

    @Autowired
    private lateinit var orgNodeKafkaTemplate: KafkaTemplate<UUID, OrgNodeChangeEvent>

    @Autowired
    private lateinit var identityKafkaTemplate: KafkaTemplate<UUID, IdentityChangeEvent>

    @Autowired
    private lateinit var orgNodeTemplate: RedisTemplate<String, OrgNode>

    @Autowired
    private lateinit var orgNodeOperations: HashOperations<String, UUID, OrgNode>

    @Autowired
    private lateinit var identityTemplate: RedisTemplate<String, Identity>

    @Autowired
    private lateinit var identityOperations: HashOperations<String, UUID, Identity>


    @BeforeEach
    fun initialize() {
        cleanup()
    }

    @AfterEach
    fun cleanup() {
        //orgNodeTemplate.execute<Any> { con -> con.flushAll() }
    }

    @Test
    fun `OrgNode should show up in redis`() {

        val event = OrgNodeChangeEvent(UUID.randomUUID(), CREATE, OrgNode(UUID.randomUUID(), "Root node"))

        logger.info("Sending event [{}]", event)

        orgNodeKafkaTemplate.send(OrgNodeChangeEvent.TOPIC_NAME, event)

        Thread.sleep(2000)

        val node = orgNodeOperations.get("OrgNode", event.orgNode.id)

        assertTrue(node != null)
        assertEquals(event.orgNode.name, node?.name)
    }

    @Test
    fun `Org Node update should be visible in redis`() {
        val event = OrgNodeChangeEvent(UUID.randomUUID(), CREATE, OrgNode(UUID.randomUUID(), "Root node"))
        orgNodeKafkaTemplate.send(OrgNodeChangeEvent.TOPIC_NAME, event)

        Thread.sleep(200)
        val node = orgNodeOperations.get("OrgNode", event.orgNode.id)

        assertTrue(node != null)
        assertEquals(event.orgNode.name, node?.name)

        val updateEvent = OrgNodeChangeEvent(UUID.randomUUID(), UPDATE, OrgNode(event.orgNode.id, "Root node 2"))
        orgNodeKafkaTemplate.send(OrgNodeChangeEvent.TOPIC_NAME, updateEvent)

        Thread.sleep(200)
        val updatedNode = orgNodeOperations.get("OrgNode", event.orgNode.id)

        assertTrue(updatedNode != null)
        assertEquals(updateEvent.orgNode.name, updatedNode?.name)
    }

    @Test
    fun `Identity should show up in redis`() {
        val event = IdentityChangeEvent(UUID.randomUUID(), CREATE, Identity(UUID.randomUUID(), "Jason Miles", "miles@example.com", HUMAN))

        identityKafkaTemplate.send(IdentityChangeEvent.TOPIC_NAME, event)

        Thread.sleep(1000)

        val identity = identityOperations.get("Identity", event.identity.id)

        assertTrue(identity != null)
        assertEquals(event.identity.name, identity?.name)
        assertEquals(event.identity.email, identity?.email)
        assertEquals(event.identity.type, identity?.type)
    }

    @Test
    fun `Identity update should be visible in redis`() {
        val event = IdentityChangeEvent(UUID.randomUUID(), CREATE, Identity(UUID.randomUUID(), "Jason Miles", "miles@example.com", HUMAN))
        identityKafkaTemplate.send(IdentityChangeEvent.TOPIC_NAME, event)

        Thread.sleep(200)
        val identity = identityOperations.get("Identity", event.identity.id)

        assertTrue(identity != null)
        assertEquals(event.identity.name, identity?.name)
        assertEquals(event.identity.email, identity?.email)
        assertEquals(event.identity.type, identity?.type)

        val updateEvent = IdentityChangeEvent(UUID.randomUUID(), UPDATE, Identity(event.identity.id, "Martin Huber", "huber@example.com", HUMAN))
        identityKafkaTemplate.send(IdentityChangeEvent.TOPIC_NAME, updateEvent)

        Thread.sleep(200)
        val updatedIdentity = identityOperations.get("Identity", event.identity.id)

        assertTrue(updatedIdentity != null)
        assertEquals(updateEvent.identity.name, updatedIdentity?.name)
        assertEquals(updateEvent.identity.email, updatedIdentity?.email)
        assertEquals(updateEvent.identity.type, updatedIdentity?.type)
    }
}