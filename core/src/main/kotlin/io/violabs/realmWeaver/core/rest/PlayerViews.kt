package io.violabs.realmWeaver.core.rest

import java.util.UUID

interface PlayerView {
    enum class Type(val clazz: Class<out PlayerView>) {
        DEFAULT(IdPlayerView::class.java),
        FULL(FullPlayerView::class.java)
    }
}

interface IdPlayerView : PlayerView {
    val id: UUID
}

interface FullPlayerView : IdPlayerView {
    val alias: String
    val email: String
}