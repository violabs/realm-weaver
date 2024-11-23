package io.violabs.realmWeaver.core.service

import io.violabs.realmWeaver.core.rest.CreatePlayerRequest
import io.violabs.realmWeaver.core.rest.IdPlayerView
import io.violabs.realmWeaver.core.rest.PlayerView
import io.violabs.realmWeaver.core.utils.VLoggable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.reflect.KClass

interface PlayerService : VLoggable {
    fun save(createRequest: CreatePlayerRequest): IdPlayerView
    fun <R : PlayerView> listPlayersByPage(pageable: Pageable, klass: KClass<R>): Page<R>
    fun <R : PlayerView> getPlayerById(id: UUID, klass: KClass<R>): R?
    fun deletePlayerById(id: UUID)
}