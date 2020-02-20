package org.linesofcode.taurus.domain

import java.util.UUID

data class IdentityRoleChangeEvent(val eventId: UUID, val action: Action, val identityRole: IdentityRole) {
    companion object {
        const val TOPIC_NAME = "identity-governance-identity-role-change"
    }
}
