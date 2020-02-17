package org.linesofcode.taurus.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

class Identity(@JsonProperty("id") val id: UUID = UUID.randomUUID(),
               @JsonProperty("name") val name: String = "",
               @JsonProperty("email") val email: String? = "",
               @JsonProperty("type") val type: IdentityType = IdentityType.HUMAN) {
    enum class IdentityType {
        HUMAN,
        MACHINE
    }
}

