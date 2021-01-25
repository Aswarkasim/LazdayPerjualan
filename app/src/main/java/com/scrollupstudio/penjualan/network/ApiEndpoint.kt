package com.scrollupstudio.penjualan.network

import com.lazday.poslaravel.data.model.cart.ResponseCartList
import com.lazday.poslaravel.data.model.cart.ResponseCartUpdate
import com.lazday.poslaravel.data.model.checkout.ResponseCheckout
import com.lazday.poslaravel.data.model.transaction.ResponseTransactionList
import com.lazday.poslaravel.data.model.transaction.detail.ResponseTransactionDetail
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentDetail
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentList
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentUpdate
import com.scrollupstudio.penjualan.data.model.login.ResponseLogin
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoint {

    @FormUrlEncoded
    @POST("login_pegawai")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @GET("agen")
    fun getAgent(): Call<ResponseAgentList>

    @Multipart
    @POST("agen")
    fun insertAgent(
        @Query("nama_toko") nama_toko: String,
        @Query("nama_pemilik") nama_pemilik: String,
        @Query("alamat") alamat: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Part gambar_toko: MultipartBody.Part

    ): Call<ResponseAgentUpdate>

    @GET("agen/{kd_agen}")
    fun getAgentDetail(
        @Path("kd_agen") kd_agen: Long
    ): Call<ResponseAgentDetail>

    @Multipart
    @POST("agen/{kd_agen}")
    fun updateAgent(
        @Path("kd_agen") kd_agen: Long,
        @Query("nama_toko") nama_toko: String,
        @Query("nama_pemilik") nama_pemilik: String,
        @Query("alamat") alamat: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Part gambar_toko: MultipartBody.Part?,
        @Query("_method") _method: String
    ): Call<ResponseAgentUpdate>


    @DELETE("agen/{kd_agen}")
    fun deleteAgent(
        @Path("kd_agen") kd_agen: Long
    ): Call<ResponseAgentUpdate>

    @FormUrlEncoded
    @POST("get_transaksi")
    fun getTransactionbyUsername(
        @Field("username") username: String
    ): Call<ResponseTransactionList>

    @FormUrlEncoded
    @POST("get_detail_transaksi")
    fun getTransactionByInvoice(
        @Field("no_faktur")no_faktur: String
    ): Call<ResponseTransactionDetail>

    @FormUrlEncoded
    @POST("get_cart")
    fun getCart(
        @Field("username") username: String
    ): Call<ResponseCartList>

    @FormUrlEncoded
    @POST("delete_item_cart")
    fun deleteItemCart(
        @Field("kd_keranjang") kd_keranjang: Long
    ): Call<ResponseCartUpdate>

    @FormUrlEncoded
    @POST("delete_cart")
    fun deleteCart(
        @Field("username") username: String
    ): Call<ResponseCartUpdate>

    @FormUrlEncoded
    @POST
    fun checkOut(
        @Field("username") username: String,
        @Field("kd_agen") kd_agen: Long
    ): Call<ResponseCheckout>
}