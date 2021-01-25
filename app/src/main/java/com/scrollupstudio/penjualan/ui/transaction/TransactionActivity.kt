package com.scrollupstudio.penjualan.ui.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scrollupstudio.penjualan.R

class TransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initActivity()
    }

    private fun initActivity() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, TransactionFragment(), "transFrag")
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        if(supportFragmentManager.findFragmentByTag("") == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TransactionFragment(), "transFrag")
                .commit()
        }else{
            finish()
        }

        return super.onSupportNavigateUp()
    }



}


