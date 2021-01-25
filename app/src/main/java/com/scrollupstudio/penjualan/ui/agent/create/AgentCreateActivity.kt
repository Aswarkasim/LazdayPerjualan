package com.scrollupstudio.penjualan.ui.agent.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lazday.poslaravel.util.GalleryHelper
import com.scrollupstudio.penjualan.R
import com.scrollupstudio.penjualan.data.Constant
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentUpdate
import com.scrollupstudio.penjualan.ui.agent.AgentMapsActivity
import com.scrollupstudio.penjualan.utils.FileUtils
import kotlinx.android.synthetic.main.activity_agent_create.*
import kotlinx.android.synthetic.main.activity_agent_create.view.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class AgentCreateActivity : AppCompatActivity(), AgentCreateContract.View {

    private var uriImage : Uri? = null
    private var pickImage = 1

    private lateinit var presenter: AgentCreatePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_create)

        presenter = AgentCreatePresenter(this)


    }

    override fun onStart() {
        super.onStart()
        if(Constant.LATITUDE.isNotEmpty()){
            edtLocation.setText("${Constant.LATITUDE}, ${Constant.LONGITUDE}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Constant.LATITUDE = ""
        Constant.LONGITUDE = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == pickImage && resultCode == Activity.RESULT_OK){
            uriImage = data!!.data
            imvImage.setImageURI(uriImage)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun initActivity() {
        supportActionBar!!.title = "Agen Baru"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun initListener() {
        edtLocation.setOnClickListener {
            startActivity(Intent(this, AgentMapsActivity::class.java))
        }

        imvImage.setOnClickListener {
            if (GalleryHelper.permissionGallery(this, this, pickImage)){
                GalleryHelper.openGallery(this)
            }
        }

        btnSubmit.setOnClickListener {
            var nameStore = edtNameStore.text
            var nameOwner = edtNameOwner.text
            var address = edtAddress.text
            var location = edtLocation.text

            if(nameStore.isNullOrEmpty() || nameOwner.isNullOrEmpty() || address.isNullOrEmpty() || location.isNullOrEmpty() || uriImage == null){
                showMessage("Lengkapi data dengan benar")
            }else{
                presenter.insertAgent(nameStore.toString(), nameOwner.toString(), address.toString(), Constant.LATITUDE, Constant.LONGITUDE, FileUtils.getFile(this, uriImage))
            }
        }
    }

    override fun onLoading(loading: Boolean) {
        when(loading){
            true -> {
                progress.visibility = View.VISIBLE
                btnSubmit.visibility = View.GONE
            }
            false -> {
                progress.visibility = View.GONE
                btnSubmit.visibility = View.VISIBLE
            }
        }
    }

    override fun onResult(responseAgentUpdate: ResponseAgentUpdate) {
        showMessage(responseAgentUpdate.msg)
        finish()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}