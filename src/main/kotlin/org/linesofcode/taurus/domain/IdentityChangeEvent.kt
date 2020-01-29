package org.linesofcode.taurus.domain

import java.util.UUID

class IdentityChangeEvent(val eventId: UUID, val action: Action, val identity: Identity) {
    companion object {
        const val TOPIC_NAME = "identity-governance-identity-change"
    }
}