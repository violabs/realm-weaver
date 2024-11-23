package io.violabs.realmWeaver.core.entity

import java.util.*

interface Player {
    val id: UUID?
    val alias: String
    val email: String
}