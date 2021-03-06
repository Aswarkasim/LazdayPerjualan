package com.scrollupstudio.penjualan.ui.cart

import com.google.android.gms.common.api.Api
import com.lazday.poslaravel.data.model.cart.ResponseCartList
import com.lazday.poslaravel.data.model.cart.ResponseCartUpdate
import com.lazday.poslaravel.data.model.checkout.ResponseCheckout
import com.scrollupstudio.penjualan.network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartPresenter (val view: CartContract.View): CartContract.Presenter{

    init {
        view.iniActivity()
    }


    override fun getCart(username: String) {
        view.onLoadingCart(true)
        ApiServices.endpoint.getCart(username)
            .enqueue(object :  Callback<ResponseCartList>{
                override fun onFailure(call: Call<ResponseCartList>, t: Throwable) {
                    view.onLoadingCart(false)
                }

                override fun onResponse(
                    call: Call<ResponseCartList>,
                    response: Response<ResponseCartList>
                ) {
                    view.onLoadingCart(false)
                    if (response.isSuccessful){
                        val responseCartList: ResponseCartList? = response.body()
                        view.onResultCart(responseCartList!!)
                    }
                }

            })
    }

    override fun deleteItemCart(kd_keranjang: Long) {
        ApiServices.endpoint.deleteItemCart(kd_keranjang)
            .enqueue(object : Callback<ResponseCartUpdate>{
                override fun onFailure(call: Call<ResponseCartUpdate>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseCartUpdate>,
                    response: Response<ResponseCartUpdate>
                ) {

                    if(response.isSuccessful){
                        val responseCartUpdate:  ResponseCartUpdate? = response.body()
                        view.onResultDelete(responseCartUpdate!!)
                    }
                }

            })
    }

    override fun deleteCart(username: String) {
        ApiServices.endpoint.deleteCart(username)
            .enqueue(object : Callback<ResponseCartUpdate>{
                override fun onFailure(call: Call<ResponseCartUpdate>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseCartUpdate>,
                    response: Response<ResponseCartUpdate>
                ) {
                    if(response.isSuccessful){
                        val responseCartUpdate: ResponseCartUpdate? = response.body()
                        view.onResultDelete(responseCartUpdate!!)
                        view.showMessage(responseCartUpdate.msg)
                    }
                }

            })
    }

    override fun checkOut(username: String, kd_agent: Long) {
        view.onLoadingCart(true)
        ApiServices.endpoint.checkOut(username, kd_agent)
            .enqueue(object : Callback<ResponseCheckout>{
                override fun onFailure(call: Call<ResponseCheckout>, t: Throwable) {
                    view.onLoadingCart(false)
                }

                override fun onResponse(
                    call: Call<ResponseCheckout>,
                    response: Response<ResponseCheckout>
                ) {
                    view.onLoadingCart(false)
                    if(response.isSuccessful){
                        val responseCheckout: ResponseCheckout? = response.body()
                        view.onResultCheckout(responseCheckout!!)
                        view.showMessage(responseCheckout.msg)
                    }
                }

            })
    }

}