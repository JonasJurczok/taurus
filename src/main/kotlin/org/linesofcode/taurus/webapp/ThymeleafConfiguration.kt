package org.linesofcode.taurus.webapp

import nz.net.ultraq.thymeleaf.LayoutDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ThymeleafConfiguration {
    @Bean
    fun layoutDialect(): LayoutDialect? {
        return LayoutDialect()
    }
}