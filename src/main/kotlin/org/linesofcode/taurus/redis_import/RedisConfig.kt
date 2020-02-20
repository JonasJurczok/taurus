package org.linesofcode.taurus.redis_import

import org.linesofcode.taurus.domain.Identity
import org.linesofcode.taurus.domain.IdentityRole
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

    companion object {
        const val ORG_NODE_KEY = "OrgNode"
        const val ORG_NODE_ROOT_KEY = "OrgNodeRoot"
        const val IDENTITY_KEY = "Identity"
        const val IDENTITY_ROLE_KEY = "IdentityRole"
    }

    @Bean
    fun orgNodeOperations(factory: RedisConnectionFactory): HashOperations<String, UUID, OrgNode> {
        return buildHashOps(factory)
    }

    @Bean
    fun identityOperations(factory: RedisConnectionFactory): HashOperations<String, UUID, Identity> {
        return buildHashOps(factory)
    }

    @Bean
    fun identityRoleOperations(factory: RedisConnectionFactory): HashOperations<String, UUID, IdentityRole> {
        return buildHashOps(factory)
    }

    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(factory)
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer<Identity>(Identity::class.java)
        template.setDefaultSerializer(template.valueSerializer)
        template.afterPropertiesSet()
        return template
    }

    private inline fun <reified T> buildHashOps(factory: RedisConnectionFactory): HashOperations<String, UUID, T> {
        val template = RedisTemplate<String, T>()
        template.setConnectionFactory(factory)
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer<T>(T::class.java)
        template.setDefaultSerializer(template.valueSerializer)
        template.afterPropertiesSet()
        return template.opsForHash<UUID, T>()
    }
}