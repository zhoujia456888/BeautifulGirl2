package com.jloveh.beautifulgirl2.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.jloveh.beautifulgirl2.R
import com.jloveh.beautifulgirl2.app.Config.UserAgent
import com.jloveh.beautifulgirl2.app.Config.mzituUrl
import com.jloveh.beautifulgirl2.app.Config.nvshensUrl
import com.jloveh.beautifulgirl2.app.base.BaseActivity
import com.jloveh.beautifulgirl2.app.ext.init
import com.jloveh.beautifulgirl2.app.ext.initFloatBtn
import com.jloveh.beautifulgirl2.data.model.bean.BeautifulImgData
import com.jloveh.beautifulgirl2.databinding.ActivityMainBinding
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
import java.text.DecimalFormat

class PicturesDetailsActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    lateinit var beautifulImgData: BeautifulImgData

    override fun layoutId() = R.layout.fragment_beautifulgirl

    var frontCoverAdapter = BeautifulImgAdapter()

    var isFirstEnter = true

    var beautifulImgDataList = mutableListOf<BeautifulImgData>()

    override fun initView(savedInstanceState: Bundle?) {

        BarUtils.setStatusBarLightMode(this, true)

        //初始化recyclerView
        var staggeredGridLayoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.init(staggeredGridLayoutManager, frontCoverAdapter).let {
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        recyclerView.itemAnimator = null

        //防止上划的时候上方出现大片间隔
        var spanCount = 3
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val first = IntArray(spanCount)
                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    staggeredGridLayoutManager.invalidateSpanAssignments()
                }
            }
        })

        refreshlayout.let {
            it.setEnableLoadMore(false)
            if (isFirstEnter) {
                isFirstEnter = false
                it.autoRefresh()//第一次进入触发自动刷新，演示效果
            }

            it.setOnRefreshListener {
                beautifulImgDataList.clear()
                analyzeData()
            }

        }

    }

    private fun analyzeData() {
        GlobalScope.launch(Dispatchers.IO) {
            beautifulImgData = intent.getParcelableExtra("FrontCoverData")!!
            when (beautifulImgData.dataType) {
                nvshensUrl -> {
                    getNvShenDetailsData(beautifulImgData.picturesDetailsUrl)
                }
                mzituUrl -> {
                    getMziTuDetailsData(beautifulImgData.picturesDetailsUrl)
                }
            }
            withContext(Dispatchers.Main) {
                frontCoverAdapter.setList(beautifulImgDataList)
                refreshlayout.finishRefresh()
            }
        }
    }

    //获取宅男女神的所以图片
    private fun getNvShenDetailsData(picturesDetailsUrl: String) {

        val doc: Document = Jsoup.connect(picturesDetailsUrl).referrer(nvshensUrl).userAgent(UserAgent).get()
        val ck_parent_divs = doc.select("div.ck-parent-div")

        //获取第一张图片的地址
        val photoOneUrl = ck_parent_divs[0].select("img[src]").attr("src")

        //截取图片的头部
        val photoUrlHead = photoOneUrl.substring(0, photoOneUrl.lastIndexOf("/") + 1)

        //获取整个图片组的图片张数
        val pageSize = doc.select("span").text().split("张")[0].toInt()
        //  LogUtils.e(pageSize)

        for (index in 0 until pageSize) {
            //大于0的话就是3位数，不足3位补0
            var indexStr = index.toString()
            if (index > 0) {
                indexStr = DecimalFormat("000").format(index)
            }

            var frontCoverImgUrl = "$photoUrlHead$indexStr.jpg"
            var picturesDetailsUrl = ""
            beautifulImgDataList.add(BeautifulImgData(nvshensUrl, frontCoverImgUrl, picturesDetailsUrl, nvshensUrl))
        }

    }

    //获取妹子图的所有图片
    private fun getMziTuDetailsData(picturesDetailsUrl: String) {
        val doc: Document = Jsoup.connect(picturesDetailsUrl).referrer(mzituUrl).userAgent(UserAgent).get()
        val placepadding = doc.select("div.main-image")
        //获取第一张图片的地址
        val photoOneUrl = placepadding[0].select("img[src]").attr("src")
        LogUtils.e(photoOneUrl)//https://imgpc.iimzt.com/2019/09/26b01.jpg

        val pagenavi = doc.select("div.pagenavi")
        var pagenavi_a = pagenavi.select("a")
        //获取整个图片组的图片张数
        var pageSize = pagenavi.select("a")[pagenavi_a.size - 2].select("span").text()

        var pageSizeInt = pageSize.toInt()

        LogUtils.e(pageSize)

        val photoUrlHead = photoOneUrl.substring(0, photoOneUrl.length - (4 + pageSize.length))



        for (index in 0 until pageSizeInt) {
            //大于0的话就是3位数，不足3位补0
            var indexStr = index.toString()
            if (index > 0) {
                indexStr = DecimalFormat("00").format(index)
            }

            var frontCoverImgUrl = "$photoUrlHead$indexStr.jpg"
            var picturesDetailsUrl = ""
            beautifulImgDataList.add(BeautifulImgData(mzituUrl, frontCoverImgUrl, picturesDetailsUrl, mzituUrl))
        }


    }
}