package org.linesofcode.taurus.infrastructure

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.KafkaTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SpringBootTest
class AbstractReadFromBeginningListenerTest {
    val topicName = "ListenerTest"
    val logger = LoggerFactory.getLogger(AbstractReadFromBeginningListenerTest::class.java)

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var adminClient: AdminClient

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var listenerRegistry: KafkaListenerEndpointRegistry

    @BeforeEach
    fun setup() {
        logger.info("Creating topic [{}].", topicName)
        adminClient.createTopics(listOf(NewTopic(topicName, 1, 1)))
    }

    @AfterEach
    fun teardown() {
        logger.info("Deleting topic [{}].", topicName)
        adminClient.deleteTopics(listOf(topicName))
    }

    @Test
    fun `listener should always read from beginning of queue`() {
        kafkaTemplate.send(topicName, "Message1")
        kafkaTemplate.send(topicName, "Message2")
        kafkaTemplate.send(topicName, "Message3")

        // instantiate the kafka listener through spring
        val listener: TestListener = applicationContext.getBean("TestListener") as TestListener

        // Start the listener
        listenerRegistry.getListenerContainer("TestListener").start()

        assertTrue(listener.latch.await(10, TimeUnit.SECONDS))
    }

    class TestListener: AbstractReadFromBeginningListener() {
        override val logger = LoggerFactory.getLogger("TestListener")

        val latch = CountDownLatch(3)

        @KafkaListener(topics = ["ListenerTest"], groupId = "ListenerTest", autoStartup = "false", id = "TestListener")
        fun processMessage(event: String) {
            logger.info("got message: {}", event)
            latch.countDown()
        }
    }
}