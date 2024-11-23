package io.violabs.realmWeaver.controller

import io.violabs.realmWeaver.TestcontainersConfiguration
import io.violabs.realmWeaver.core.rest.CreatePlayerRequest
import io.violabs.realmWeaver.core.rest.IdPlayerView
import io.violabs.realmWeaver.player.PlayerController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class PlayerControllerTest(
    @Autowired private val playerController: PlayerController
) {

    @Test
    fun `should create a player`() {
        // given
        val request = CreatePlayerRequest("skeletor", "skeletor@violabs.io")

        // when
        val response: IdPlayerView? = playerController.createPlayer(request).body
        println(response)

        // then
        assert(response != null)
    }
}