package io.violabs.realmWeaver.core.utils

import mu.KLogger
import mu.KLogging

private val K_LOGGING = KLogging()

private val LOG_MAP = mutableMapOf<String, KLogger>()

interface VLoggable {
    val log: KLogger
        get() {
            val name = this::class.simpleName ?: "DefaultLogger"
            return LOG_MAP.getOrPut(name) { K_LOGGING.logger(name) }
        }
}