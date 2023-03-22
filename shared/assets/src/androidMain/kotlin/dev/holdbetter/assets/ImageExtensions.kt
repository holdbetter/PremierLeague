package dev.holdbetter.assets

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget

fun ImageView.load(urlString: String) {
    Glide.with(this.context)
        .load(urlString)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}

fun Uri.getFutureBitmap(context: Context): FutureTarget<Bitmap> {
    return Glide.with(context)
        .asBitmap()
        .load(this)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .submit()
}

fun ImageView.loadWithPlaceholder(urlString: String, @DrawableRes drawableRes: Int) {
    Glide.with(this.context)
        .load(urlString)
        .placeholder(drawableRes)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}