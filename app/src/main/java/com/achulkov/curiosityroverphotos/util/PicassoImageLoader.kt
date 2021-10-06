package com.achulkov.curiosityroverphotos.util

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PicassoImageLoader @Inject constructor(private val picasso: Picasso) : ImageLoader {

    override fun load(url: String?): ImageLoader.ImageRequest {
        return PicassoImageRequest(picasso.load(url))
    }

    override fun cancelRequest(view: ImageView) {
        picasso.cancelRequest(view)
    }



    private inner class PicassoImageRequest(val request: RequestCreator) : ImageLoader.ImageRequest {

        override fun into(view: ImageView) {
            request.into(view)
        }

        override fun into(view: ImageView, callback: Callback) {
            request.into(view, callback)
        }

        override fun resize(width: Int, height: Int) :ImageLoader.ImageRequest{
            request.resize(width,height)
            return this
        }

        override fun centerCrop(): ImageLoader.ImageRequest {
            request.centerCrop()
            return this
        }

        override fun centerInside() : ImageLoader.ImageRequest {
            request.centerInside()
            return this
        }

        override fun placeholder(imageResource: Int): ImageLoader.ImageRequest {
            request.placeholder(imageResource)
            return this
        }

        override fun error(imageResource: Int): ImageLoader.ImageRequest {
            request.error(imageResource)
            return this
        }

        override fun noPlaceHolder(): ImageLoader.ImageRequest {
            request.noPlaceholder()
            return this
        }


    }

}