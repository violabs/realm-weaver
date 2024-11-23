package io.violabs.realmWeaver.player

import io.violabs.realmWeaver.core.rest.CreatePlayerRequest
import io.violabs.realmWeaver.core.rest.FullPlayerView
import io.violabs.realmWeaver.core.rest.IdPlayerView
import io.violabs.realmWeaver.core.rest.PlayerView
import io.violabs.realmWeaver.core.service.PlayerService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class DefaultPlayerService(private val playerRepo: PlayerRepo) : PlayerService {
    // MASKING ON EMAIL EVENTUALLY
    @Transactional
    override fun save(createRequest: CreatePlayerRequest): IdPlayerView {
        log.info { "Saving player: $createRequest" }

        requireNotNull(createRequest.alias) { "Alias is required" }
        requireNotNull(createRequest.email) { "Email is required" }

        val player = PlayerEntity(
            alias = createRequest.alias!!,
            email = createRequest.email!!
        ).also(playerRepo::save)

        requireNotNull(player.id) { "Player id is required" }

        return object : IdPlayerView {
            override val id: UUID = player.id
        }
    }

    override fun <R : PlayerView> listPlayersByPage(pageable: Pageable, klass: KClass<R>): Page<R> {
        log.info {
            "Listing players by " +
                "page: ${pageable.pageNumber}," +
                " size: ${pageable.pageSize}, " +
                "class: ${klass.simpleName}"
        }

        return playerRepo.findAllBy(pageable, klass.java)
    }

    override fun <R : PlayerView> getPlayerById(id: UUID, klass: KClass<R>): R? {
        log.info { "Getting player. id: $id, class: ${klass.simpleName}" }
        return playerRepo.findById(id, klass.java)
    }

    override fun deletePlayerById(id: UUID) {
        log.info { "Deleting player by id $id" }
        playerRepo.deleteById(id)
    }
}