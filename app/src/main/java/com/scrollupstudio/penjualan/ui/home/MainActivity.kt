package com.scrollupstudio.penjualan.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Toast
import com.scrollupstudio.penjualan.R
import com.scrollupstudio.penjualan.data.database.PrefsManager
import com.scrollupstudio.penjualan.ui.agent.AgentActivity
import com.scrollupstudio.penjualan.ui.login.LoginActivity
import com.scrollupstudio.penjualan.ui.transaction.TransactionActivity
import com.scrollupstudio.penjualan.ui.user.UserActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {

    lateinit var prefsManager: PrefsManager
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsManager = PrefsManager(this)
        presenter = MainPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        when(prefsManager.prefsIsLogin){
            true -> {
                crvUser.visibility = View.VISIBLE
                btnLogin.visibility = View.GONE
            }
            false ->{
                crvUser.visibility = View.GONE
                btnLogin.visibility = View.VISIBLE
            }
        }
    }

    override fun initListener() {

        crvTransaction.setOnClickListener {
            if(prefsManager.prefsIsLogin){
                startActivity(Intent(this, TransactionActivity::class.java))
            }else{
                showMessage("Anda belum login")
            }
        }

        crvAgen.setOnClickListener {
            if (prefsManager.prefsIsLogin){
                startActivity(Intent(this, AgentActivity::class.java))
            }else{
                showMessage("Anda belum login")
            }
        }

        crvUser.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }
        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}