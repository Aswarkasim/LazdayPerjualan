package com.scrollupstudio.penjualan.ui.transaction

import com.lazday.poslaravel.data.model.transaction.ResponseTransactionList
import com.scrollupstudio.penjualan.network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionPresenter(val view: TransactionContract.View): TransactionContract.Presenter {

    init {
        view.initFragment()
    }

    override fun getTransactionByUsername(username: String) {
        view.onLoading(true)
        ApiServices.endpoint.getTransactionbyUsername(username)
            .enqueue(object : Callback<ResponseTransactionList>{
                override fun onFailure(call: Call<ResponseTransactionList>, t: Throwable) {
                    view.onLoading(false)
                }

                override fun onResponse(
                    call: Call<ResponseTransactionList>,
                    response: Response<ResponseTransactionList>
                ) {
                    view.onLoading(false)
                    if(response.isSuccessful){
                        val responseTransactionList: ResponseTransactionList? = response.body()
                        view.onResult(responseTransactionList!!)
                    }
                }

            })
    }
}