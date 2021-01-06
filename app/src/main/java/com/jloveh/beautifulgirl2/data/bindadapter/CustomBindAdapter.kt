package com.jloveh.beautifulgirl2.data.bindadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.jloveh.beautifulgirl2.app.Config.Accept
import com.jloveh.beautifulgirl2.app.Config.AcceptEncoding
import com.jloveh.beautifulgirl2.app.Config.AcceptLanguage
import com.jloveh.beautifulgirl2.app.Config.SecFetchDest
import com.jloveh.beautifulgirl2.app.Config.UpgradeInsecureRequests
import com.jloveh.beautifulgirl2.app.Config.UserAgent
import com.jloveh.beautifulgirl2.app.Config.mzituUrl
import com.jloveh.beautifulgirl2.app.Config.nvshensUrl
import com.jloveh.beautifulgirl2.app.Config.presentPage


/**
 * 自定义BindAdapter
 */
object CustomBindAdapter {

    @BindingAdapter(value = ["imageUrl"])
    @JvmStatic
    fun imageUrl(view: ImageView, url: String) {

        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

        Glide.with(view).load(
            GlideUrl(
                url, LazyHeaders.Builder()
                    .addHeader("Accept", Accept)
                    .addHeader("Accept-Encoding", AcceptEncoding)
                    .addHeader("Accept-Language", AcceptLanguage)
                    .addHeader("Sec-Fetch-Dest", SecFetchDest)
                    .addHeader("Upgrade-Insecure-Requests", UpgradeInsecureRequests)
                    .addHeader("Referer", if (presentPage == nvshensUrl) nvshensUrl else mzituUrl)
                    .addHeader("User-Agent", UserAgent)
                    .build()
            )
        )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerInside()
            .transition(withCrossFade(factory))
            .into(view)


        /* view.load(url) {
             crossfade(true)
             addHeader("Referer", if (presentPage == nvshensUrl) nvshensUrl else mzituUrl)
             addHeader(
                 "User-Agent",
                 "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36"
             )
         }*/
    }

}