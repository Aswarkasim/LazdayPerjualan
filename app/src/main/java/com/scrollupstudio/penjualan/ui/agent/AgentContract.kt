package com.scrollupstudio.penjualan.ui.agent

import com.scrollupstudio.penjualan.data.model.agent.DataAgent
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentList
import com.scrollupstudio.penjualan.data.model.agent.ResponseAgentUpdate

interface AgentContract {

    interface presenter{
        fun getAgent()
        fun deleteAgent(kd_agen: Long)
    }

    interface View{
        fun initActivity()
        fun initListener()
        fun onLoadingAgent(loading: Boolean)
        fun onResultAgent(responseAgenList: ResponseAgentList)
        fun onResultDelete(responseAgentUpdate: ResponseAgentUpdate)
        fun showDialogDelete(dataAgent: DataAgent, position: Int)
        fun showDialogDetail(dataAgent: DataAgent, position: Int)
        fun setMessage(message: String)
    }
}