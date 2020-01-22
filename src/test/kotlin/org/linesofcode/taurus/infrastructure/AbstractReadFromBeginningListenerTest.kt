package org.linesofcode.taurus.infrastructure

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SpringBootTest
class AbstractReadFromBeginningListenerTest {

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var kafkaAdmin: KafkaAdmin

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var listenerRegistry: KafkaListenerEndpointRegistry

    @Test
    fun listenerShouldAlwaysReadFromBeginning() {
        val topicName = "ListenerTest"
        val adminClient = AdminClient.create(kafkaAdmin.config)
        adminClient.createTopics(listOf(NewTopic(topicName, 1, 1)))

        kafkaTemplate.send(topicName, "Message1")
        kafkaTemplate.send(topicName, "Message2")
        kafkaTemplate.send(topicName, "Message3")

        val latch = CountDownLatch(3)
        val listener: TestListener = applicationContext.getBean("TestListener") as TestListener
        listener.latch = latch

        listenerRegistry.getListenerContainer("TestListener").start()

        assertTrue(latch.await(10, TimeUnit.SECONDS))

        adminClient.deleteTopics(listOf(topicName))
    }

    class TestListener: AbstractReadFromBeginningListener() {
        override val logger = LoggerFactory.getLogger("TestListener")

        lateinit var latch: CountDownLatch

        @KafkaListener(topics = ["ListenerTest"], groupId = "ListenerTest", autoStartup = "false", id = "TestListener")
        fun processMessage(event: String) {
            logger.info("got message: {}", event)
            latch.countDown()
        }
    }
}

@Configuration
class AbstractReadFromBeginningListenerTestConfig {
    @Bean("TestListener")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    fun listener(): AbstractReadFromBeginningListenerTest.TestListener {
        return AbstractReadFromBeginningListenerTest.TestListener()
    }
}
