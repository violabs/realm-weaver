package io.violabs.realmWeaver.player

import io.violabs.realmWeaver.core.rest.CreatePlayerRequest
import io.violabs.realmWeaver.core.rest.IdPlayerView
import io.violabs.realmWeaver.core.rest.PlayerView
import io.violabs.realmWeaver.core.service.PlayerService
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("players")
class PlayerController(private val playerService: PlayerService) {
    @PostMapping
    fun createPlayer(@RequestBody request: CreatePlayerRequest): ResponseEntity<IdPlayerView> {
        val view = playerService.save(request)

        return ResponseEntity.ok(view)
    }

    @GetMapping
    fun getPlayers(
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
        @RequestParam("view-type", defaultValue = "DEFAULT") viewType: PlayerView.Type
    ): ResponseEntity<List<PlayerView>> {
        val pageRequest = PageRequest.of(page, size)
        val views = playerService.listPlayersByPage(pageRequest, viewType.clazz.kotlin).content

        return ResponseEntity.ok(views)
    }

    @GetMapping("{id}")
    fun getPlayerById(
        @PathVariable("id") id: UUID,
        @RequestParam("view-type", defaultValue = "DEFAULT") viewType: PlayerView.Type
    ): ResponseEntity<PlayerView> {
        return playerService
            .getPlayerById(id, viewType.clazz.kotlin)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }
}