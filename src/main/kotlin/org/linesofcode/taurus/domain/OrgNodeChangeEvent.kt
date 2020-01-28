package org.linesofcode.taurus.domain

import java.util.UUID

data class OrgNodeChangeEvent(val eventId: UUID, val action: OrgNodeAction, val orgNode: OrgNode) {
    enum class OrgNodeAction {
        CREATE,
        UPDATE,
        DELETE
    }
}