package dev.holdbetter.core_di_impl

import android.app.Application
import androidx.fragment.app.Fragment

actual typealias ContextProvider = Fragment

actual fun ContextProvider.getModuleProvider(): ModuleProvider {
    try {
        return application() as ModuleProvider
    } catch (e: ClassCastException) {
        throw IllegalStateException(
            """Wrong application used. 
            |Make sure that your application class is ModuleProvider
            |Your application is: ${application()::class.qualifiedName}"""
                .trimMargin()
        )
    }
}

fun Fragment.application(): Application = requireActivity().application