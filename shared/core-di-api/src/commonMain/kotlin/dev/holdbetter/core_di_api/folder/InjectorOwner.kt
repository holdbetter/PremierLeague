package dev.holdbetter.core_di_api.folder

import dev.zacsweers.metro.MembersInjector
import kotlin.reflect.KClass

interface InjectorOwner {
    val injectors: Map<KClass<*>, MembersInjector<*>>
}