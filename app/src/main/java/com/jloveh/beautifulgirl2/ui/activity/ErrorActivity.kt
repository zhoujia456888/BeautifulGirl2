package com.jloveh.beautifulgirl2.ui.activity

import android.os.Bundle
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jloveh.beautifulgirl2.R
import com.jloveh.beautifulgirl2.app.base.BaseActivity
import com.jloveh.beautifulgirl2.databinding.ActivityErrorBinding
import kotlinx.android.synthetic.main.activity_error.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat

class ErrorActivity : BaseActivity<BaseViewModel, ActivityErrorBinding>() {
    override fun layoutId() = R.layout.activity_error

    override fun initView(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarLightMode(this, true)
        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        errorRestart.clickNoRepeat {
            config?.run {
                CustomActivityOnCrash.restartApplication(this@ErrorActivity, this)
            }
        }
        errorSendError.clickNoRepeat {
            CustomActivityOnCrash.getStackTraceFromIntent(intent)?.let {
                ToastUtils.showShort("点这个没用，气死你")
            }


        }
    }


}