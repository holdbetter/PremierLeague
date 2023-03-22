package dev.holdbetter.assets

import android.content.Context
import android.content.res.Resources
import androidx.annotation.DimenRes

val Int.dp get() = this / Resources.getSystem().displayMetrics.density

val Int.px get() = (this * Resources.getSystem().displayMetrics.density)

fun Context.px(@DimenRes dimen: Int): Float = resources.getDimension(dimen)

fun Context.dp(@DimenRes dimen: Int): Float = px(dimen) / resources.displayMetrics.density