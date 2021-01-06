package com.jloveh.beautifulgirl2.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.jloveh.beautifulgirl2.R
import com.jloveh.beautifulgirl2.app.Config.UserAgent
import com.jloveh.beautifulgirl2.app.Config.mzituUrl
import com.jloveh.beautifulgirl2.app.Config.presentPage
import com.jloveh.beautifulgirl2.app.base.BaseFragment
import com.jloveh.beautifulgirl2.app.ext.init
import com.jloveh.beautifulgirl2.app.ext.initFloatBtn
import com.jloveh.beautifulgirl2.data.model.bean.BeautifulImgData
import com.jloveh.beautifulgirl2.databinding.FragmentBeautifulgirlBinding
import com.jloveh.beautifulgirl2.ui.adapter.BeautifulImgAdapter
import com.jloveh.beautifulgirl2.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_beautifulgirl.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MZiTuFragment : BaseFragment<MainViewModel, FragmentBeautifulgirlBinding>() {

    var frontCoverAdapter = BeautifulImgAdapter()

    var isFirstEnter = true

    var page = 1

    var beautifulImgDataList = mutableListOf<BeautifulImgData>()
    lateinit var beautifulImgData: BeautifulImgData

    override fun layoutId() = R.layout.fragment_beautifulgirl

    override fun initView(savedInstanceState: Bundle?) {
        //初始化recyclerView
        recyclerView.init(GridLayoutManager(context, 3), frontCoverAdapter).let {
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }

        refreshlayout.let {
            if (isFirstEnter) {
                isFirstEnter = false
                it.autoRefresh()//第一次进入触发自动刷新，演示效果
            }

            it.setOnRefreshListener {
                page = 1
                getMziTuData()
            }
            it.setOnLoadMoreListener {
                page += 1
                getMziTuData()
            }
        }
    }


    //获取妹子图数据
    fun getMziTuData() {
        presentPage = mzituUrl
        GlobalScope.launch(Dispatchers.IO) {
            var url = "${mzituUrl}page/$page/"
         //   LogUtils.e("mzituUrl:" + url)
            val doc: Document = Jsoup.connect(url).userAgent(UserAgent).get()

            var postlist = doc.select("div.postlist").select("li")
          //   LogUtils.e("postlist:${postlist.size}")

            beautifulImgDataList.clear()

            postlist.forEach {
                var beautifulImgUrl = it.select("img").attr("abs:data-original")

                var picturesDetailsUrl = it.select("a").attr("abs:href")

                beautifulImgData = BeautifulImgData(mzituUrl, beautifulImgUrl, picturesDetailsUrl,url)
                beautifulImgDataList.add(beautifulImgData)
            }

            withContext(Dispatchers.Main) {
                if (page == 1) {
                    frontCoverAdapter.setList(beautifulImgDataList)
                    refreshlayout.finishRefresh()
                } else {
                    frontCoverAdapter.addData(beautifulImgDataList)
                    refreshlayout.finishLoadMore()
                }
            }
        }


    }


}