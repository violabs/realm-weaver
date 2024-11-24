package io.violabs.realmWeaver.controller

import io.violabs.realmWeaver.TestcontainersConfiguration
import io.violabs.realmWeaver.core.rest.CreatePlayerRequest
import io.violabs.realmWeaver.core.rest.IdPlayerView
import io.violabs.realmWeaver.player.PlayerController
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

const val ALIAS = "skeletor"
const val EMAIL = "skeletor@violabs.io"

@SpringBootTest
@Import(TestcontainersConfiguration::class)
class PlayerControllerCreateTest(
    @Autowired private val playerController: PlayerController
) {

    @ParameterizedTest(name = "should throw an exception when {0} is missing")
    @MethodSource("exceptionTestCases")
    fun `should throw an exception when field is missing`(target: String, alias: String?, email: String?) {
        // given
        val request = CreatePlayerRequest(alias = alias, email = email)

        // when
        val exception = assertThrows<IllegalArgumentException> {
            playerController.createPlayer(request)
        }

        // then
        assert(exception.message == "createPlayerRequest.$target is required") {
            """
                | EXPECT: createPlayerRequest.$target is required
                | ACTUAL: ${exception.message}
            """.trimMargin()
        }
    }

    @Test
    fun `should create a player`() {
        // given
        val request = CreatePlayerRequest(ALIAS, EMAIL)

        // when
        val response: IdPlayerView? = playerController.createPlayer(request).body
        println(response)

        // then
        assert(response != null)
    }

    companion object {
        @JvmStatic
        fun exceptionTestCases() = listOf(
            //            target, alias, email
            Arguments.of("alias", null, EMAIL),
            Arguments.of("email", ALIAS, null)
        )
    }
}
