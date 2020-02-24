package org.linesofcode.taurus.webapp.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.linesofcode.taurus.domain.Identity
import org.linesofcode.taurus.domain.IdentityRole
import java.util.UUID

data class OrgNodeDTO(@JsonProperty("id") val id: UUID = UUID.randomUUID(),
                 @JsonProperty("name") val name: String = "",
                 @JsonProperty("parent") val parent: UUID? = null,
                 @JsonProperty("children") val children: Set<UUID> = emptySet(),
                 @JsonProperty("manager") val manager: Identity? = null,
                 @JsonProperty("members") val members: Set<Identity> = emptySet(),
                 @JsonProperty("version") val version: Int = 0,
                 @JsonProperty("roles") val roles: Set<IdentityRole> = emptySet()) {
}