package io.violabs.realmWeaver.player

import io.violabs.realmWeaver.core.rest.CreatePlayerRequest
import io.violabs.realmWeaver.core.rest.IdPlayerView
import io.violabs.realmWeaver.core.service.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("players")
class PlayerController(private val playerService: PlayerService) {
    @PostMapping
    fun createPlayer(@RequestBody request: CreatePlayerRequest): ResponseEntity<IdPlayerView> {
        val view = playerService.save(request)

        return ResponseEntity.ok(view)
    }
}