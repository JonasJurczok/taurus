package org.linesofcode.taurus

import org.linesofcode.taurus.infrastructure.AbstractReadFromBeginningListenerTest
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class TestConfigurarion {

    @Bean("TestListener")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    fun listener(): AbstractReadFromBeginningListenerTest.TestListener {
        return AbstractReadFromBeginningListenerTest.TestListener()
    }

}