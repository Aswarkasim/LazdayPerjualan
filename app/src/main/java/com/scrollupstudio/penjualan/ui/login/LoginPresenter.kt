package com.scrollupstudio.penjualan.ui.login

import com.scrollupstudio.penjualan.data.database.PrefsManager
import com.scrollupstudio.penjualan.data.model.login.DataLogin
import com.scrollupstudio.penjualan.data.model.login.ResponseLogin
import com.scrollupstudio.penjualan.network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter (val view: LoginContract.View): LoginContract.Presenter{

    init {
        view.initActivity()
        view.iniListener()
        view.onLoading(false)
    }

    override fun doLogin(username: String, password: String) {
        view.onLoading(true)
        ApiServices.endpoint.loginUser(username, password)
            .enqueue(object : Callback<ResponseLogin>{
                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    view.onLoading(false)
                }

                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    view.onLoading(false)
                    if(response.isSuccessful){
                        val responseLogin: ResponseLogin? = response.body()
                        view.showMessage(responseLogin!!.msg)

                        if(responseLogin!!.status){
                            view.onResult(responseLogin)
                        }


                    }
                }

            })
    }

    override fun setPrefs(prefsManager: PrefsManager, dataLogin: DataLogin) {
        prefsManager.prefsIsLogin = true
        prefsManager.prefsUsename = dataLogin.username!!
        prefsManager.prefsNamaPegawai = dataLogin.nama_pegawai!!
        prefsManager.prefsJk = dataLogin.jk!!
        prefsManager.prefsAlamat = dataLogin.alamat!!
        prefsManager.prefsIsAktif = dataLogin.is_aktif!!
    }

}