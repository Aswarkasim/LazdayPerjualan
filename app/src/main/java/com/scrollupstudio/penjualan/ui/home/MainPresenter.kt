package com.scrollupstudio.penjualan.ui.home

class MainPresenter (val view: MainContract.View){

    init {
        view.initListener()
    }
}