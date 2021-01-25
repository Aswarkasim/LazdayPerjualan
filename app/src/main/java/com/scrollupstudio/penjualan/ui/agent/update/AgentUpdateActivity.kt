package com.scrollupstudio.penjualan.ui.agent.update

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lazday.poslaravel.util.GalleryHelper
import com.scrollupstudio.penjualan.R
import com.scrollupstudio.penjualan.data.Constant
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentDetail
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentUpdate
import com.scrollupstudio.penjualan.ui.agent.AgentMapsActivity
import com.scrollupstudio.penjualan.utils.FileUtils
import com.scrollupstudio.penjualan.utils.GlideHelper
import com.scrollupstudio.penjualan.utils.MapsHelper
import kotlinx.android.synthetic.main.activity_agent_create.*

class AgentUpdateActivity : AppCompatActivity(), AgentUpdateContract.View {

    private val tagLog: String = "AgentUpdateActivity"

    private var uriImage: Uri? = null
    private  val pickImage = 1

    lateinit var presenter: AgentUpdatePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_create)

        supportActionBar!!.title = ""
        supportActionBar!!.elevation = 0f
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()

        MapsHelper.permissionMap(this, this)
        presenter = AgentUpdatePresenter(this)
        presenter.getDetail(Constant.AGENT_ID)
    }

    override fun onResume() {
        super.onResume()
        if(Constant.LATITUDE.isNotEmpty()){
            edtLocation?.setText("${Constant.LONGITUDE}, ${Constant.LONGITUDE}")
        }
    }

    fun init(){
        txvTitle.text = "Agen Edit"
        btnSubmit.text = "Simpan Perubahan"

        edtLocation.setOnClickListener {
            startActivity(Intent(this, AgentMapsActivity::class.java))
        }
        imvImage.setOnClickListener {
            if(GalleryHelper.permissionGallery(this, this, pickImage)){
                GalleryHelper.openGallery(this)
            }
        }

        btnSubmit.setOnClickListener {
            val nameStore = edtNameStore.text
            val nameOwner = edtNameOwner.text
            val address = edtAddress.text
            val location = edtLocation.text

            if(nameStore.isNullOrEmpty() || nameOwner.isNullOrEmpty()|| address.isNullOrEmpty()||location.isNullOrEmpty()){
                showMessage("Lengkapi data dengan benar")
            }else{
                presenter.updateAgent(
                    Constant.AGENT_ID,
                    nameStore.toString(),
                    nameOwner.toString(),
                    address.toString(),
                    Constant.LATITUDE,
                    Constant.LONGITUDE,
                    FileUtils.getFile(this, uriImage)
                )
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

    override fun onResultDetail(responseAgentEdit: ResponseAgentDetail) {
        val agent = responseAgentEdit.dataAgent

        edtNameStore.setText(agent.nama_toko)
        edtNameOwner.setText(agent.nama_pemilik)
        edtAddress.setText(agent.alamat)
        edtLocation.setText("${agent.longitude}, ${agent.longitude}")

        Constant.LATITUDE = agent.latitude!!
        Constant.LONGITUDE = agent.longitude!!

        GlideHelper.setImage(this, agent.gambar_toko!!, imvImage)
    }

    override fun onResultUpdate(responseAgentUpdate: ResponseAgentUpdate) {
        showMessage(responseAgentUpdate.msg)
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        Constant.LATITUDE = ""
        Constant.LONGITUDE = ""
    }
}