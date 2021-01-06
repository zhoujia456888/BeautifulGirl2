package com.jloveh.beautifulgirl2.app.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jloveh.beautifulgirl2.R
import com.jloveh.beautifulgirl2.app.Config
import me.bzcoder.easyglide.EasyGlide
import me.bzcoder.easyglide.config.GlideConfigImpl
import me.bzcoder.easyglide.progress.CircleProgressView
import me.bzcoder.easyglide.progress.OnProgressListener

object LoadBeautifulUtils {

    fun loadBeautifulImg(context:Context,imageUrl:String,upDataUrl:String,imageView:ImageView,circleProgressView: CircleProgressView){
        EasyGlide.loadImage(
            context, GlideConfigImpl
                .builder()
                .url(
                    GlideUrl(
                        imageUrl, LazyHeaders.Builder()
                            .addHeader("Referer", upDataUrl)
                            .addHeader("User-Agent", Config.UserAgent)
                            .build()
                    )
                )
               // .errorPic(R.mipmap.ic_img_error)
                .imageView(imageView)
                .isFitCenter(true)
                .progressListener(object : OnProgressListener {
                    override fun onProgress(isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) {
                        // 跟踪进度
                        if (isComplete) {
                            circleProgressView.visibility = View.GONE
                        } else {
                            circleProgressView.visibility = View.VISIBLE
                        }
                        circleProgressView.progress = percentage
                    }
                })
                .requestListener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        circleProgressView.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        circleProgressView.visibility = View.GONE
                        return false
                    }
                })
                .build()
        )
    }
}