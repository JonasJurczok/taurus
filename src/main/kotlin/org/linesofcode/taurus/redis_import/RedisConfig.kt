package org.linesofcode.taurus.redis_import

import org.linesofcode.taurus.domain.OrgNode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.util.UUID

@Configuration
class RedisConfig {

    @Bean("OrgNodeTemplate")
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, OrgNode> {
        val template = RedisTemplate<String, OrgNode>()
        template.setConnectionFactory(factory)
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer<OrgNode>(OrgNode::class.java)
        template.setDefaultSerializer(template.valueSerializer)
        return template
    }

    @Bean("OrgNodeOperations")
    fun orgNodeOperations(redisTemplate: RedisTemplate<String, OrgNode>): HashOperations<String, UUID, OrgNode> {
        return redisTemplate.opsForHash<UUID, OrgNode>()
    }
}