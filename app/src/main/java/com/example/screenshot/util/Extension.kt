package com.example.screenshot.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest

fun ImageView.loadImageWithCoil(uri: String?) {
    // Initialize your ImageLoader here if you haven't
    val imageLoader = ImageLoader.Builder(context).build()

    val request = ImageRequest.Builder(context)
        .data(uri)
        .memoryCachePolicy(CachePolicy.ENABLED)  // Enable memory cache
        .diskCachePolicy(CachePolicy.ENABLED)
        .target(this)
        // any other configurations
        .build()

    imageLoader.enqueue(request)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.showToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}