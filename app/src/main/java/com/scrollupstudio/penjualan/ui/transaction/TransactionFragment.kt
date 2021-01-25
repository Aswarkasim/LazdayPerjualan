package com.scrollupstudio.penjualan.ui.transaction

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lazday.poslaravel.data.model.transaction.DataTransaction
import com.lazday.poslaravel.data.model.transaction.ResponseTransactionList
import com.scrollupstudio.penjualan.R
import com.scrollupstudio.penjualan.data.Constant
import com.scrollupstudio.penjualan.data.database.PrefsManager
import com.scrollupstudio.penjualan.ui.cart.CartActivity
import com.scrollupstudio.penjualan.ui.transaction.detail.TransactionDetailFragment
import kotlinx.android.synthetic.main.fragment_transaction.*


class TransactionFragment : Fragment(), TransactionContract.View {

    lateinit var prefManager: PrefsManager
    lateinit var transactionAdapter: TransactionAdapter
    lateinit var presenter: TransactionPresenter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_transaction, container, false)
        prefManager  = PrefsManager(view.context)
        presenter = TransactionPresenter(this)
        initListener(view)
        return  view
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar!!.title = "Transaksi"
        presenter.getTransactionByUsername(prefManager.prefsUsename)
    }




    override fun initFragment() {
        transactionAdapter = TransactionAdapter(requireContext(), arrayListOf()){
            dataTransaction, position ->
            onClickTransaction(dataTransaction.no_faktur!!)
        }
    }

    override fun initListener(view: View) {
        val rcvTransaction = view.findViewById<RecyclerView>(R.id.rcvTransaction)
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe_transaction)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_transaction)

        rcvTransaction!!.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        swipe.setOnRefreshListener {
            presenter.getTransactionByUsername(prefManager.prefsUsename)
        }
        fab.setOnClickListener {
            startActivity(Intent(this.context, CartActivity::class.java))
        }

    }

    override fun onLoading(loading: Boolean) {
        when(loading){
            true -> swipe_transaction.isRefreshing = true
            false -> swipe_transaction.isRefreshing = false
        }
    }

    override fun onResult(responseTransactionList: ResponseTransactionList) {
        val dataTransaction: List<DataTransaction> = responseTransactionList.dataTransaction
        transactionAdapter.setData(dataTransaction)
    }

    override fun onClickTransaction(invoice: String) {
        Constant.INVOICE = invoice
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.container,
                TransactionDetailFragment(), "transDetail")
            .commit()

    }


}