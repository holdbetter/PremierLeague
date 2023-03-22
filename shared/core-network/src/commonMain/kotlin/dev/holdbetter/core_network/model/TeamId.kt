package dev.holdbetter.core_network.model

import kotlin.jvm.JvmInline

@JvmInline
value class TeamId(override val value: String) : Parameter {

    companion object {
        const val name = "id"
    }

    override val name: String
        get() = Companion.name
}