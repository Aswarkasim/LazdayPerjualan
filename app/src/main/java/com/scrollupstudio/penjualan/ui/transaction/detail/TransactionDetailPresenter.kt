package com.scrollupstudio.penjualan.ui.transaction.detail

import android.util.Log
import com.lazday.poslaravel.data.model.transaction.detail.ResponseTransactionDetail
import com.scrollupstudio.penjualan.network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionDetailPresenter(val view: TransactionDetailContract.View): TransactionDetailContract.Presenter {

    init {
        view.initFragment()
    }
    override fun getTransactionByInvoice(no_faktur: String) {
        view.onLoadingDetail(true)
        ApiServices.endpoint.getTransactionByInvoice(no_faktur)
            .enqueue(object : Callback<ResponseTransactionDetail>{
                override fun onFailure(call: Call<ResponseTransactionDetail>, t: Throwable) {
                    Log.d("TransactionPresenter", t.toString())
                    view.onLoadingDetail(false)
                }

                override fun onResponse(
                    call: Call<ResponseTransactionDetail>,
                    response: Response<ResponseTransactionDetail>
                ) {
                   Log.e("TransactionPresenter", response.toString())
                    view.onLoadingDetail(false)
                    if(response.isSuccessful){
                        val responseTransactionDetail: ResponseTransactionDetail? = response.body()
                        view.onResultDetail(responseTransactionDetail!!)
                    }
                }

            })
    }
}