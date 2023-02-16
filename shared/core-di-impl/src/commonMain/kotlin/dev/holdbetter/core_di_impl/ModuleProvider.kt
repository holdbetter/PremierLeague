package dev.holdbetter.core_di_impl

import dev.holdbetter.core_di_api.folder.HasSharedDependencies
import kotlin.reflect.KClass

typealias MutableModuleDependencies = MutableMap<KClass<out HasSharedDependencies>, HasSharedDependencies>
typealias ModuleDependencies = Map<KClass<out HasSharedDependencies>, HasSharedDependencies>

interface ModuleProvider {
    val moduleMap: ModuleDependencies
}

// e.g Fragment, UIApplication
expect class ContextProvider

expect fun ContextProvider.getModuleProvider(): ModuleProvider

inline fun <reified T : HasSharedDependencies> ContextProvider.findModuleDependency(): T {
    try {
        return getModuleProvider().moduleMap.getValue(T::class) as T
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException(
            """Make sure that ${T::class.qualifiedName} is registered 
                |in common (main) module as HasSharedDependencies"""
                .trimMargin()
        )
    }
}