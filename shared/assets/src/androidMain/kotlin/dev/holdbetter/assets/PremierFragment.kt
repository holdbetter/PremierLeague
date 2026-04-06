package dev.holdbetter.assets

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment

open class PremierFragment(@LayoutRes layout: Int) : Fragment(layout) {

    val windowState by lazy { WindowState(requireActivity().window) }

    open val insetsHandler = InsetsHandler { view, windowState ->
        ViewCompat.setOnApplyWindowInsetsListener(view) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updatePadding(
                top = insets.top,
                bottom = if (windowState.isNavigationHidden) 0 else insets.bottom,
                left = insets.left,
                right = insets.right
            )

            windowInsets
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val root = requireView()
        insetsHandler(root, windowState)
    }
}
