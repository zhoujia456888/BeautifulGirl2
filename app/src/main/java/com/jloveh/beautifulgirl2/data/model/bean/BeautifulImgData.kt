package com.jloveh.beautifulgirl2.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class BeautifulImgData(
    var dataType: String,//数据类型//宅男女神还是妹子图
    var beautifulImgUrl: String,//封面图片Url
    var picturesDetailsUrl: String,//封面图片的点击跳转链接
    var upDataUrl: String,//上一级的页面URL,用于Glide的Referer显示
) : Parcelable
