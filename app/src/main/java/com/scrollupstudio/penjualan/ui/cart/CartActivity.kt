package com.scrollupstudio.penjualan.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lazday.poslaravel.data.model.cart.DataCart
import com.lazday.poslaravel.data.model.cart.ResponseCartList
import com.lazday.poslaravel.data.model.cart.ResponseCartUpdate
import com.lazday.poslaravel.data.model.checkout.ResponseCheckout
import com.scrollupstudio.penjualan.R
import com.scrollupstudio.penjualan.data.Constant
import com.scrollupstudio.penjualan.data.database.PrefsManager
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity(), CartContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private lateinit var prefsManager: PrefsManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartPresenter: CartPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar!!.title = "Keranjang"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        prefsManager = PrefsManager(this)
        cartAdapter = CartAdapter(this, arrayListOf()){ dataCart: DataCart, position: Int ->
            cartPresenter.deleteItemCart(dataCart.kd_keranjang!!)
        }
        cartPresenter = CartPresenter(this)
        cartPresenter.getCart(prefsManager.prefsUsename)
    }

    override fun onResume() {
        super.onResume()

        if(Constant.IS_CHANGED){
            Constant.IS_CHANGED = false
            cartPresenter.getCart(prefsManager.prefsUsename)
            cartPresenter.getCart(prefsManager.prefsUsename)
            edtAgent.setText(Constant.AGENT_NAME)
        }
    }

    override fun iniActivity() {
        txvReset.visibility = View.GONE
        edtAgent.visibility = View.GONE

        rcvCart.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        swipe.setOnRefreshListener (this)
        btnAdd.setOnClickListener (this)
        txvReset.setOnClickListener (this)
        edtAgent.setOnClickListener(this)
        btnCheckout.setOnClickListener(this)
    }

    override fun onClick(view: View?){
        when(view!!.id){
           // btnAdd.id -> startActivity(Intent(this, CartAdapter::class.java))
            txvReset.id -> showDialog()
         //   edtAgent.id -> startActivity(Intent(this, AgentSea))
            btnCheckout.id -> {
                if(cartAdapter.dataCart.isNullOrEmpty()){
                    showMessage("Keranjang kosong")
                }else{
                    if(edtAgent.text.isNullOrEmpty()){
                        edtAgent.error = "Tidak boleh kosong"
                    }else{
                        cartPresenter.checkOut(prefsManager.prefsUsename, Constant.AGENT_ID)
                    }
                }
            }
        }
    }

    override fun onLoadingCart(loading: Boolean) {
        when(loading){
            true -> swipe.isRefreshing = true
            false -> swipe.isRefreshing = true
        }
    }

    override fun onResultCart(responseCartList: ResponseCartList) {
        val dataCart: List<DataCart> = responseCartList.dataCart
        if(dataCart.isNullOrEmpty()){
            txvReset.visibility = View.GONE
            edtAgent.visibility = View.GONE
        }else{
            cartAdapter.updateCart(dataCart)
            txvReset.visibility = View.VISIBLE
            edtAgent.visibility = View.VISIBLE
        }
    }

    override fun onResultDelete(responseCartUpdate: ResponseCartUpdate) {
        cartPresenter.getCart(prefsManager.prefsUsename)
        cartAdapter.removeAll()
    }

    override fun onLoadingCheckout(loading: Boolean) {
        when(loading){
            true -> {
                btnCheckout.isEnabled = false
                btnCheckout.setText("Memuat...")
            }
            false -> {
                btnCheckout.isEnabled = true
                btnCheckout.setText("Checkout")
            }
        }
    }

    override fun onResultCheckout(responseCheckout: ResponseCheckout) {
        cartPresenter.getCart(prefsManager.prefsUsename)
        cartAdapter.removeAll()
    }

    override fun showDialog() {
       val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Konfirmasi")
        dialog.setMessage("Hapus semua item dalam keranjang?")

        dialog.setPositiveButton("Hapus"){dialog, which ->
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }



    override fun onRefresh() {
       cartPresenter.getCart(prefsManager.prefsUsename)
    }

    override fun onDestroy() {
        super.onDestroy()
        Constant.CART_ID = 0
        Constant.AGENT_NAME = ""
    }
}