package com.scrollupstudio.penjualan.ui.login

import com.scrollupstudio.penjualan.data.database.PrefsManager
import com.scrollupstudio.penjualan.data.model.login.DataLogin
import com.scrollupstudio.penjualan.data.model.login.ResponseLogin

interface LoginContract {

    interface Presenter{
        fun doLogin(username: String, password: String)
        fun setPrefs(prefsManager: PrefsManager, dataLogin: DataLogin)
    }

    interface  View{
        fun initActivity()
        fun iniListener()
        fun onLoading(loading: Boolean)
        fun onResult(responseLogin: ResponseLogin)
        fun showMessage(message: String)
    }
}