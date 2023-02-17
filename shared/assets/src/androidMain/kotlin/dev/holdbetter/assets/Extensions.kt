package dev.holdbetter.assets

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.load(urlString: String) {
    Glide.with(this.context)
        .load(urlString)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}

fun ImageView.loadWithPlaceholder(urlString: String, @DrawableRes drawableRes: Int) {
    Glide.with(this.context)
        .load(urlString)
        .placeholder(drawableRes)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}