package org.linesofcode.taurus.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.linesofcode.taurus.webapp.dto.OrgNodeDTO
import java.util.UUID

data class OrgNode(@JsonProperty("id") val id: UUID = UUID.randomUUID(),
                   @JsonProperty("name") val name: String = "",
                   @JsonProperty("parent") val parent: UUID? = null,
                   @JsonProperty("children") val children: Set<UUID> = emptySet(),
                   @JsonProperty("manager") val manager: UUID? = null,
                   @JsonProperty("members") val members: Set<UUID> = emptySet(),
                   @JsonProperty("version") val version: Int = 0) {

    fun toDTO(manager: Identity? = null, members: Set<Identity> = emptySet()): OrgNodeDTO {
        return OrgNodeDTO(id, name, parent, children, manager, members)
    }
}