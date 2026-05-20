package dev.holdbetter.core_di_api.folder

import dev.zacsweers.metro.MembersInjector

@Suppress("UNCHECKED_CAST")
fun InjectorOwner.inject(instance: Any) {
    (injectors[instance::class] as? MembersInjector<Any>)?.injectMembers(instance)
}