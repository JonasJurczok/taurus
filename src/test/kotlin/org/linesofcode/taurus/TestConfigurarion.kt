package org.linesofcode.taurus

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.linesofcode.taurus.domain.IdentityChangeEvent
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.linesofcode.taurus.infrastructure.AbstractReadFromBeginningListenerTest
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class TestConfigurarion {

    @Bean("TestListener")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    fun listener(): AbstractReadFromBeginningListenerTest.TestListener {
        return AbstractReadFromBeginningListenerTest.TestListener()
    }

    @Bean
    fun kafkaAdminClient(admin: KafkaAdmin): AdminClient {
        return AdminClient.create(admin.config)
    }

    @Bean
    fun staffOrgNodeChangeTopic(): NewTopic {
        return NewTopic(OrgNodeChangeEvent.TOPIC_NAME, 1, 1)
    }

    @Bean
    fun identityChangeTopic(): NewTopic {
        return NewTopic(IdentityChangeEvent.TOPIC_NAME, 1, 1)
    }
}