package com.scrollupstudio.penjualan.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lazday.poslaravel.data.model.cart.DataCart
import com.scrollupstudio.penjualan.R
import com.scrollupstudio.penjualan.utils.GlideHelper
import kotlinx.android.synthetic.main.adapter_cart.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CartAdapter (val context: Context, var dataCart: ArrayList<DataCart>,
                    val clickListener: (DataCart, Int)->Unit): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val view = view
        fun bind(dataCart: DataCart){
            view.txvCategory.text = dataCart.kategori
            view.txvNameProduct.text = dataCart.nama_produk
            view.txvPrice.text = "${dataCart.harga_rupiah} x ${dataCart.jumlah}"

            val total: Double = dataCart.jumlah!!.toDouble() * dataCart.harga!!.toDouble()
            val totalRupiah: String = NumberFormat.getNumberInstance(Locale.GERMANY).format(total)
            view.txvTotal.text = "Rp. $totalRupiah"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_cart, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataCart.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(dataCart[position])
        GlideHelper.setImage(context, dataCart[position].gambar_produk!!, holder.view.imvImage)
        holder.view.imvDelete.setOnClickListener {
            clickListener(dataCart[position], position)
            removeCart(position)
        }
    }

    private fun removeCart(position: Int) {
        dataCart.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataCart.size)
    }

    fun updateCart(newCart: List<DataCart>){
        dataCart.clear()
        dataCart.addAll(newCart)
        notifyDataSetChanged()
    }

    fun removeAll(){
        dataCart.clear()
        notifyDataSetChanged()
    }
}