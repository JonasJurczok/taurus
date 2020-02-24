package org.linesofcode.taurus.webapp.dto

import org.linesofcode.taurus.domain.OrgNode
import org.linesofcode.taurus.webapp.IdentityService
import org.linesofcode.taurus.webapp.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class OrgNodeToOrgNodeDTOTransformer(@Autowired val identityService: IdentityService,
                                     @Autowired val roleService: RoleService) {

    fun toDTO(node: OrgNode): OrgNodeDTO {
        return node.toDTO(manager = node.manager?.let { identityService.getById(it) },
                members = identityService.getByIds(node.members),
                roles = roleService.getByOrgNodeId(node.id))
    }

    fun toDTO(nodes: Collection<OrgNode>): Set<OrgNodeDTO> {
        return nodes.map { toDTO(it) }.toSet()
    }
}