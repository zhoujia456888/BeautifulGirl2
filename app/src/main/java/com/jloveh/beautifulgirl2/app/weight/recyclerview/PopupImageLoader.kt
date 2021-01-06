package com.jloveh.beautifulgirl2.app.weight.recyclerview

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.jloveh.beautifulgirl2.app.Config
import com.jloveh.beautifulgirl2.data.model.bean.BeautifulImgData
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File

class PopupImageLoader(var upDataUrl: String) : XPopupImageLoader {
    override fun loadImage(position: Int, uri: Any, imageView: ImageView) {

        var beautifulImgData = uri as BeautifulImgData
        var url = beautifulImgData.beautifulImgUrl

        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

        //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
        Glide.with(imageView).load(
            GlideUrl(
                url, LazyHeaders.Builder()
                    .addHeader("Referer", upDataUrl)
                    .addHeader("User-Agent", Config.UserAgent)
                    .build()
            )
        )
            .centerInside()
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .apply(
                RequestOptions()
                    .override(Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(imageView)

    }

    override fun getImageFile(context: Context, uri: Any): File? {
        try {
            var beautifulImgData = uri as BeautifulImgData
            var url = beautifulImgData.beautifulImgUrl

            return Glide.with(context).downloadOnly().load(url).submit().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}