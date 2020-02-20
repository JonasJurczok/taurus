package org.linesofcode.taurus.domain

import java.util.UUID

data class IdentityChangeEvent(val eventId: UUID, val action: Action, val identity: Identity) {
    companion object {
        const val TOPIC_NAME = "staff-identity-change"
    }
}