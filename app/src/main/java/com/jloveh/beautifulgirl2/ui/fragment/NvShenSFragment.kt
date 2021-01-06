package com.jloveh.beautifulgirl2.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.jloveh.beautifulgirl2.R
import com.jloveh.beautifulgirl2.app.Config.nvshensUrl
import com.jloveh.beautifulgirl2.app.Config.presentPage
import com.jloveh.beautifulgirl2.app.Config.UserAgent
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

class NvShenSFragment : BaseFragment<MainViewModel, FragmentBeautifulgirlBinding>() {


    var frontCoverAdapter = BeautifulImgAdapter()

    var isFirstEnter = true

    var page = 1

    var beautifulgirlList = mutableListOf<BeautifulImgData>()
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
                getNvShenData()
            }
            it.setOnLoadMoreListener {
                page += 1
                getNvShenData()
            }
        }
    }

    //获取宅男女神数据
    fun getNvShenData() {
        presentPage = nvshensUrl
        GlobalScope.launch(Dispatchers.IO) {
            var url = "$nvshensUrl$page.html"
            val doc: Document = Jsoup.connect(url).referrer(nvshensUrl).userAgent(UserAgent).get()

            var ckInitems = doc.select("div.ck-initem")

            beautifulgirlList.clear()

            ckInitems.forEachIndexed { index, element ->
                var beautifulImgUrl = element.select("mip-img").attr("abs:src")
                var picturesDetailsUrl = element.select("a.ck-link").attr("abs:href")
                beautifulgirlList.add(BeautifulImgData(nvshensUrl, beautifulImgUrl, picturesDetailsUrl,url))
            }

            withContext(Dispatchers.Main) {
                if (page == 1) {
                    frontCoverAdapter.setList(beautifulgirlList)
                    refreshlayout.finishRefresh()
                } else {
                    frontCoverAdapter.addData(beautifulgirlList)
                    refreshlayout.finishLoadMore()
                }
            }

//            LogUtils.e(frontCoverAdapter.data)


        }
    }
}