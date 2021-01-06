package com.jloveh.beautifulgirl2.app

import com.blankj.utilcode.util.SPUtils

object Config {


    //彩蛋的地址
    var webUrlSp = SPUtils.getInstance("webUrl")

    val nvshensUrl = "https://m.nvshens.org/gallery/"
    val mzituUrl = "https://m.mzitu.com/"

    var presentPage = nvshensUrl

    var Accept= "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
    var AcceptEncoding="gzip, deflate, sdch"
    var AcceptLanguage= "zh-CN,zh;q=0.8"
    var SecFetchDest="document"
    var UpgradeInsecureRequests="1"
    val UserAgent =
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"



}