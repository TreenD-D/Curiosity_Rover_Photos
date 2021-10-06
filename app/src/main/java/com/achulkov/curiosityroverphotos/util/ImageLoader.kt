package com.achulkov.curiosityroverphotos.util

import android.widget.ImageView
import com.squareup.picasso.Callback

interface ImageLoader {
    fun load(url: String?): ImageRequest
    fun cancelRequest(view: ImageView)


    interface ImageRequest {
        fun into(view: ImageView)
        fun into(view: ImageView, callback: Callback)
        fun resize(width: Int, height:Int) : ImageRequest
        fun centerCrop() : ImageRequest
        fun placeholder(imageResource : Int) : ImageRequest
        fun error(imageResource : Int) : ImageRequest
        fun noPlaceHolder() : ImageRequest

        fun centerInside(): ImageRequest
    }
}