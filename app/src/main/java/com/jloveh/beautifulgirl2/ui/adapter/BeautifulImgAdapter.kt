package com.jloveh.beautifulgirl2.ui.adapter

import android.content.Intent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jloveh.beautifulgirl2.R
import com.jloveh.beautifulgirl2.app.util.LoadBeautifulUtils.loadBeautifulImg
import com.jloveh.beautifulgirl2.app.weight.recyclerview.PopupImageLoader
import com.jloveh.beautifulgirl2.data.model.bean.BeautifulImgData
import com.jloveh.beautifulgirl2.ui.activity.PicturesDetailsActivity
import com.jloveh.beautifulgirl2.ui.popup.BigImageViewerPopup
import com.lxj.xpopup.XPopup
import me.bzcoder.easyglide.progress.CircleProgressView

//首页的封面图Adapter
class BeautifulImgAdapter : BaseQuickAdapter<BeautifulImgData, BaseViewHolder>(R.layout.item_beautiful_girl) {

    override fun convert(holder: BaseViewHolder, item: BeautifulImgData) {
        var imageView = holder.getView<ImageView>(R.id.img_beautiful)
        var cpBeautiful = holder.getView<CircleProgressView>(R.id.cp_beautiful)
        var imageUrl = item.beautifulImgUrl
        var upDataUrl = item.upDataUrl

        loadBeautifulImg(context,imageUrl,upDataUrl, imageView, cpBeautiful)

        holder.itemView.setOnClickListener {
            if (item.picturesDetailsUrl.isEmpty()) {
                //如果没有详情链接，那么就直接显示大图
                var photoUrls = this.data

                XPopup.Builder(context)
                    .asCustom(
                        BigImageViewerPopup(context)
                            .setSrcView(imageView, holder.adapterPosition)
                            .setImageUrls(photoUrls.toList())
                            .setXPopupImageLoader(PopupImageLoader(upDataUrl))
                            .isShowIndicator(true)
                            .isShowSaveButton(true)
                            .isInfinite(true)
                            .setSrcViewUpdateListener { popupView, position ->
                                if (recyclerView.getChildAt(position) != null) {
                                    var constraintLayout = recyclerView.getChildAt(position) as ConstraintLayout
                                    var imageView = constraintLayout.getChildAt(1) as ImageView //注意这里，加了CircleProgressView之后ImageView变成1了
                                    popupView.updateSrcView(imageView)
                                }
                            }
                    )
                    .show()
            } else {
                var intent = Intent(context, PicturesDetailsActivity::class.java)
                intent.putExtra("FrontCoverData", item)
                context.startActivity(intent)
            }
        }

    }

}