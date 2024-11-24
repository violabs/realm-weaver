package io.violabs.realmWeaver.player

import io.violabs.realmWeaver.core.rest.PlayerView
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlayerRepo : CrudRepository<PlayerEntity, UUID>, PagingAndSortingRepository<PlayerEntity, UUID> {
    fun <T: PlayerView> findAllBy(pageable: Pageable, type: Class<T>): Page<T>
    fun <T : PlayerView> findById(id: UUID, clazz: Class<T>): T?
}