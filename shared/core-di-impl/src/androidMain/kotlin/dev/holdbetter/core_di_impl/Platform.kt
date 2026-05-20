package dev.holdbetter.core_di_impl

import android.app.Application
import androidx.fragment.app.Fragment
import dev.holdbetter.core_di_api.folder.InjectorOwner

actual typealias ContextProvider = Fragment

fun Fragment.application(): Application = requireActivity().application

fun Fragment.injector(): InjectorOwner = application() as InjectorOwner