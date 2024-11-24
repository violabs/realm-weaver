package io.violabs.realmWeaver.controller

import io.violabs.realmWeaver.TestcontainersConfiguration
import io.violabs.realmWeaver.core.rest.CreatePlayerRequest
import io.violabs.realmWeaver.core.rest.IdPlayerView
import io.violabs.realmWeaver.core.rest.PlayerView
import io.violabs.realmWeaver.player.PlayerController
import io.violabs.realmWeaver.player.PlayerEntity
import io.violabs.realmWeaver.player.PlayerRepo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.util.UUID

private const val ALIAS = "skeletor"
private const val EMAIL = "skeletor@violabs.io"
private val DEFAULT_PLAYER_1 = PlayerEntity(alias = ALIAS, email = EMAIL)
private val DEFAULT_PLAYER_2 = PlayerEntity(alias = "she-ra", email = "she.ra@violabs.io")
private val DEFAULT_PLAYER_3 = PlayerEntity(alias = "he-man", email = "he.man223@violabs.io")

private val TWO_PLAYERS = listOf(DEFAULT_PLAYER_1, DEFAULT_PLAYER_2)
private val THREE_PLAYERS = listOf(DEFAULT_PLAYER_1, DEFAULT_PLAYER_2, DEFAULT_PLAYER_3)

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

@SpringBootTest
@Import(TestcontainersConfiguration::class)
class PlayerControllerListTest(
    @Autowired private val playerController: PlayerController,
    @Autowired private val playerRepo: PlayerRepo
) {
    @AfterEach
    fun tearDown() {
        playerRepo.deleteAll()
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listPageAmountScenarios")
    fun `should list limited players`(scenario: ListPageScenario) {
        // given
        playerRepo.saveAll(scenario.players)

        // when
        val response = playerController.getPlayers(
            scenario.page,
            scenario.size,
            PlayerView.Type.DEFAULT
        ).body

        // then
        assert(response != null) {
            "response is null"
        }

        assert(response!!.size == scenario.expectedAmount) {
            """
                | EXPECT: ${scenario.expectedAmount}
                | ACTUAL: ${response.size}
            """.trimMargin()
        }
    }

    @ParameterizedTest(name = "should list players as view type {0}")
    @EnumSource(names = ["DEFAULT", "FULL"])
    fun `should list players by type`(viewType: PlayerView.Type) {
        // given
        playerRepo.saveAll(TWO_PLAYERS.map { it.copy() })

        // when
        val response = playerController.getPlayers(
            page = 0,
            size = 1,
            viewType = viewType
        ).body

        // then
        assert(response != null) {
            "response is null"
        }

        assert(response!!.all { viewType.clazz.isInstance(it) }) {
            "expected all to have view type $viewType, but they were ${response.map { it::class }}"
        }
    }

    companion object {

        @JvmStatic
        fun listPageAmountScenarios() = listOf(
            ListPageScenario(1, 0, 1, TWO_PLAYERS),
            ListPageScenario(2, 0, 2, TWO_PLAYERS),
            ListPageScenario(2, 0, 2, THREE_PLAYERS),
            ListPageScenario(2, 0, 3, TWO_PLAYERS),
            ListPageScenario(2, 0, 2, TWO_PLAYERS),
            ListPageScenario(1, 1, 2, THREE_PLAYERS),
        ).map { Arguments.of(it) }
    }

    /**
     * @param expectedAmount the expected amount of records
     * @param page the page number
     * @param size the size of the page
     * @param initialPlayers the initial players
     */
    class ListPageScenario(
        val expectedAmount: Int,
        val page: Int,
        val size: Int,
        initialPlayers: List<PlayerEntity> = listOf(DEFAULT_PLAYER_1, DEFAULT_PLAYER_2)
    ) {
        /**
         * The players to be saved in the database. Requires a copy of the initial players to avoid
         * conflicts with the shared instance.
         */
        val players: List<PlayerEntity> = initialPlayers.map { it.copy() }

        /**
         * The testing scenario can be represented as a string.
         * @return a string representation of the scenario
         */
        override fun toString(): String {
            return "should return $expectedAmount records when page is $page and size is $size"
        }
    }
}

@SpringBootTest
@Import(TestcontainersConfiguration::class)
class PlayerControllerGetByIdTest(
    @Autowired private val playerController: PlayerController,
    @Autowired private val playerRepo: PlayerRepo
) {
    @AfterEach
    fun tearDown() {
        playerRepo.deleteAll()
    }

    @Test
    fun `should return a 404 if not found`() {
        //given
        val id = UUID.randomUUID()

        //when
        val response = playerController.getPlayerById(id, PlayerView.Type.DEFAULT)

        //then
        assert(response.statusCode.value() == 404) {
            "expected 404 status code, but got ${response.statusCode}"
        }
    }

    @ParameterizedTest(name = "should return the player by type {0}")
    @EnumSource(names = ["DEFAULT", "FULL"])
    fun `should return the player by type`(viewType: PlayerView.Type) {
        //given
        val player = playerRepo.save(DEFAULT_PLAYER_1.copy())

        //when
        val response = playerController.getPlayerById(player.id!!, viewType)

        //then
        assert(response.statusCode.is2xxSuccessful) {
            "expected 2xx status code, but got ${response.statusCode}"
        }

        assert(viewType.clazz.isInstance(response.body)) {
            "expected response to be of type ${viewType.clazz}, but got ${response.body!!::class}"
        }
    }
}