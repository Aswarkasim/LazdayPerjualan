package com.scrollupstudio.penjualan.ui.home

import android.os.Message

interface MainContract {

    interface View{
        fun initListener()
        fun showMessage(message: String)
    }
}