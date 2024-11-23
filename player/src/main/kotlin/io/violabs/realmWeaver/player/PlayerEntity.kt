package io.violabs.realmWeaver.player

import io.violabs.realmWeaver.core.entity.Player
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "player")
data class PlayerEntity(
    override val alias: String,
    override val email: String,
    @Id @GeneratedValue(generator = "uuid4")
    override val id: UUID? = null
) : Player