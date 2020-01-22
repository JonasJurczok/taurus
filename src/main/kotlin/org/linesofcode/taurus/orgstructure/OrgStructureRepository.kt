package org.linesofcode.taurus.orgstructure

import java.util.UUID

class OrgStructureRepository {
    private val orgNodes = HashMap<UUID, OrgNode>()

    fun createOrgNode(node: OrgNode) {
        orgNodes.put(node.id, node)
    }

    fun updateOrgNode(node: OrgNode) {
        orgNodes.replace(node.id, node)
    }

    fun deleteOrgNode(node: OrgNode) {
        orgNodes.remove(node.id)
    }
}