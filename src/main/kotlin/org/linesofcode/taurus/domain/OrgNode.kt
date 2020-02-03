package org.linesofcode.taurus.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class OrgNode(@JsonProperty("id") val id: UUID = UUID.randomUUID(),
                   @JsonProperty("name") val name: String = "",
                   @JsonProperty("parent") val parent: UUID? = null,
                   @JsonProperty("children") val children: Set<UUID> = emptySet())