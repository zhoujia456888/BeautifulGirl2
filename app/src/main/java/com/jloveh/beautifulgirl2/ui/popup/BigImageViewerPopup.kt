package com.jloveh.beautifulgirl2.ui.popup

import android.content.Context
import com.jloveh.beautifulgirl2.R
import com.lxj.xpopup.core.ImageViewerPopupView
import kotlinx.android.synthetic.main.popup_big_image_viewer.view.*

class BigImageViewerPopup(context: Context): ImageViewerPopupView(context){
    override fun getImplLayoutId()= R.layout.popup_big_image_viewer

    override fun onCreate() {
        super.onCreate()
        bgColor = context.resources.getColor(R.color.image_balck)

        btn_save_image.setOnClickListener {
            save()
        }

    }

}