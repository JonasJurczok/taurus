package org.linesofcode.taurus.domain

import java.util.UUID

data class OrgNodeChangeEvent(val eventId: UUID, val action: Action, val orgNode: OrgNode) {
    companion object {
        const val TOPIC_NAME = "staff-org-node-change"
    }
}