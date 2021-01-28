package com.jloveh.beautifulgirl2.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.*
import com.jloveh.beautifulgirl2.R
import com.jloveh.beautifulgirl2.app.Config.webUrlSp
import com.jloveh.beautifulgirl2.app.base.BaseActivity
import com.jloveh.beautifulgirl2.app.ext.bindViewPager2
import com.jloveh.beautifulgirl2.app.ext.init
import com.jloveh.beautifulgirl2.databinding.ActivityMainBinding
import com.jloveh.beautifulgirl2.ui.fragment.MZiTuFragment
import com.jloveh.beautifulgirl2.ui.fragment.NvShenSFragment
import com.jloveh.beautifulgirl2.viewmodel.MainViewModel
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.include_viewpager.*


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    var exitTime = 0L

    //fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf(NvShenSFragment(), MZiTuFragment())

    //标题集合
    var mDataList: ArrayList<String> = arrayListOf("宅男女神", "妹子图")

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        BarUtils.setStatusBarLightMode(this, true)

        //设置按两下退出程序
        initBack()

        //初始化ViewPage2
        view_pager.init(this, fragments).offscreenPageLimit = fragments.size

        //初始化 magic_indicator
        magic_indicator.bindViewPager2(view_pager, mDataList)

        btn_into_web.setOnClickListener {
            LogUtils.e("点击了")

            var webUrlSp = webUrlSp.getString("webUrl", "https://www.98awwyou21.xyz")
            webUrlSp = if (webUrlSp.contains("http")) webUrlSp else "https://$webUrlSp"

            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url: Uri = Uri.parse(webUrlSp)
            intent.data = content_url
            startActivity(intent)
        }

        btn_into_web.setOnLongClickListener {
            XPopup.Builder(this)
                .asInputConfirm(
                    "请输入地址",
                    "如果不能访问，那就用自带浏览器进去98tang.com，复制网页上的地址粘贴到这里.",
                    "${webUrlSp.getString("webUrl", "https://www.98awwyou21.xyz")}",
                    "https://www.98tang.com"
                ) {
                    webUrlSp.put("webUrl", it)
                }.show()
            return@setOnLongClickListener false
        }

    }

    //设置按两下退出程序
    private fun initBack() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    ToastUtils.showShort("再按一次退出程序")
                    exitTime = System.currentTimeMillis()
                } else {
                    finish()
                }
            }
        })
    }


}