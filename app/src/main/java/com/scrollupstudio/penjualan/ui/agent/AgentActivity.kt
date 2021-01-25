package com.scrollupstudio.penjualan.ui.agent

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.scrollupstudio.penjualan.R
import com.scrollupstudio.penjualan.data.Constant
import com.scrollupstudio.penjualan.data.model.agent.DataAgent
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentList
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentUpdate
import com.scrollupstudio.penjualan.ui.agent.create.AgentCreateActivity
import com.scrollupstudio.penjualan.ui.agent.update.AgentUpdateActivity
import com.scrollupstudio.penjualan.utils.GlideHelper
import com.scrollupstudio.penjualan.utils.MapsHelper
import kotlinx.android.synthetic.main.content_agent.*
import kotlinx.android.synthetic.main.dialog_agent.view.*

class AgentActivity : AppCompatActivity(), AgentContract.View, OnMapReadyCallback {

    lateinit var presenter: AgentPresenter
    lateinit var agentAdapter: AgentAdapter
    lateinit var agent: DataAgent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent)
        setSupportActionBar(findViewById(R.id.toolbar))


        presenter = AgentPresenter(this)



    }

    override fun onStart() {
        super.onStart()
        presenter.getAgent()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun initActivity() {
        supportActionBar!!.title = "Agen"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        MapsHelper.permissionMap(this, this)
    }

    override fun initListener() {
        agentAdapter = AgentAdapter(this, arrayListOf()){

            dataAgent: DataAgent, position: Int, type: String ->

            agent = dataAgent

            when (type){
                "update" -> startActivity(Intent(this, AgentUpdateActivity::class.java))
                "delete" -> showDialogDelete(dataAgent, position)
                "detail" -> showDialogDetail(dataAgent, position)
            }
        }

        rcvAgent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = agentAdapter
        }

        swipe.setOnRefreshListener {
            presenter.getAgent()
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            startActivity(Intent(this, AgentCreateActivity::class.java))
        }
    }

    override fun onLoadingAgent(loading: Boolean) {
        when(loading){
            true -> swipe.isRefreshing = true
            false -> swipe.isRefreshing = false
        }
    }

    override fun onResultAgent(responseAgenList: ResponseAgentList) {
        val dataAgent: List<DataAgent> = responseAgenList.dataAgent
        agentAdapter.setData(dataAgent)
    }

    override fun onResultDelete(responseAgentUpdate: ResponseAgentUpdate) {
        setMessage(responseAgentUpdate.msg)
    }

    override fun showDialogDelete(dataAgent: DataAgent, position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Konfirmasi")
        dialog.setMessage("Hapus ${dataAgent.nama_toko}?")

        dialog.setPositiveButton("Hapus"){ dialog, which ->
            presenter.deleteAgent(Constant.AGENT_ID)
            agentAdapter.removeAgent((position))
            dialog.dismiss()
        }

        dialog.setNegativeButton("Batal"){ dialog, which->
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showDialogDetail(dataAgent: DataAgent, position: Int) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_agent, null)

        GlideHelper.setImage(applicationContext, dataAgent.gambar_toko!!, view.imvStore)

        view.txvNameStore.text = dataAgent.nama_toko
        view.txvNameStore.text = dataAgent.nama_pemilik
        view.txvAddress.text = dataAgent.alamat

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        view.imvClose.setOnClickListener {
            supportFragmentManager.beginTransaction().remove(mapFragment).commit()
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun setMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(agent.latitude!!.toDouble(), agent.longitude!!.toDouble())
        googleMap.addMarker(MarkerOptions().position(latLng).title(agent.nama_toko))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }
}