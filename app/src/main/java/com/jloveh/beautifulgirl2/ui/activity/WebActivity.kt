package com.jloveh.beautifulgirl2.ui.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.blankj.utilcode.util.BarUtils
import com.jloveh.beautifulgirl2.R
import com.jloveh.beautifulgirl2.app.Config.webUrlSp
import com.jloveh.beautifulgirl2.app.base.BaseActivity
import com.jloveh.beautifulgirl2.databinding.ActivityWebBinding
import com.jloveh.beautifulgirl2.viewmodel.MainViewModel
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : BaseActivity<MainViewModel, ActivityWebBinding>() {

    override fun layoutId() = R.layout.activity_web

    lateinit var mAgentWeb: AgentWeb

    override fun initView(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarLightMode(this, true)

        var webUrlSp = webUrlSp.getString("webUrl", "https://www.98awwyou21.xyz")
        webUrlSp = if (webUrlSp.contains("http")) webUrlSp else "https://$webUrlSp"

        AgentWebConfig.clearDiskCache(this)

        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .createAgentWeb()
            .ready()
            .go(webUrlSp)

        activityBack()
    }

    private fun activityBack() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!mAgentWeb.back()) {
                    finish()
                }
            }
        })
    }


    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

}