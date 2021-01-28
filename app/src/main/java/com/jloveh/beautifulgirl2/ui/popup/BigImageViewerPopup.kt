package com.jloveh.beautifulgirl2.ui.popup

import android.R.attr.bitmap
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.blankj.utilcode.util.LogUtils
import com.jloveh.beautifulgirl2.R
import com.lxj.xpopup.core.ImageViewerPopupView
import kotlinx.android.synthetic.main.popup_big_image_viewer.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileInputStream


class BigImageViewerPopup(context: Context) : ImageViewerPopupView(context) {
    override fun getImplLayoutId() = R.layout.popup_big_image_viewer

    override fun onCreate() {
        super.onCreate()
        bgColor = context.resources.getColor(R.color.image_balck)

        btn_save_image.setOnClickListener {
            save()
        }

        tv_share.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                var url = urls[if (isInfinite) position % urls.size else position]

                var source = imageLoader.getImageFile(context, url)

                var bitmap = BitmapFactory.decodeStream(FileInputStream(source))

                val uri = Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, null, null))

                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.type = "image/*"
                context.startActivity(Intent.createChooser(intent, "分享到"))
            }
        }
    }

}