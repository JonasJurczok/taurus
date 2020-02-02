package org.linesofcode.taurus.webapp

import org.linesofcode.taurus.domain.Action
import org.linesofcode.taurus.domain.Action.UPDATE
import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.domain.OrgNodeChangeEvent
import org.linesofcode.taurus.redis_import.RedisConfig
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.ORG_NODE_KEY
import org.linesofcode.taurus.redis_import.RedisConfig.Companion.ORG_NODE_ROOT_KEY
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.ArrayList
import java.util.HashSet
import java.util.UUID

@Service
class OrgNodeService {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<UUID, OrgNodeChangeEvent>

    @Autowired
    private lateinit var orgNodeOperations: HashOperations<String, UUID, OrgNode>

    /**
     * Create a new OrgNode.
     * If parent is set, the new orgNode will be added as a child to the parent.
     */
    fun create(orgNode: OrgNode) {
        val events = mutableListOf<OrgNodeChangeEvent>()
        events.add(OrgNodeChangeEvent(UUID.randomUUID(), Action.CREATE, orgNode))

        if (orgNode.parent != null) {
            val parent = getById(orgNode.parent)
                    ?: throw IllegalArgumentException("Parent with id ${orgNode.parent} does not exist.")

            // add org node to children
            val updatedParent = OrgNode(parent.id, parent.name, parent.parent, parent.children.plus(orgNode.id))
            events.add(OrgNodeChangeEvent(UUID.randomUUID(), UPDATE, updatedParent))
        }
        events.forEach {kafkaTemplate.send(OrgNodeChangeEvent.TOPIC_NAME, it.orgNode.id, it)}
    }

    fun getById(id: UUID): OrgNode? {
        return orgNodeOperations.get(ORG_NODE_KEY, id)
    }

    fun getAll(): Set<OrgNode> {
        // TODO: this should be streamed.
        return orgNodeOperations.values(ORG_NODE_KEY).toHashSet()
    }

    fun getRootNodes(): Set<OrgNode> {
        return orgNodeOperations.values(ORG_NODE_ROOT_KEY).sortedBy { node -> node.name }.toHashSet()
    }

    fun getChildrenById(id: UUID): Set<OrgNode> {
        val node = orgNodeOperations.get(ORG_NODE_KEY, id)

        return orgNodeOperations.multiGet(ORG_NODE_KEY, ArrayList<UUID>(node?.children?.toMutableList())).toSet()
    }
}